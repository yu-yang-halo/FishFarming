package com.farmingsocket;


import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.farmingsocket.SPackage.DeviceType;
import com.farmingsocket.manager.UIManager;

import cn.fuck.fishfarming.utils.ConstantUtils;


public class TcpCoreManager implements IReceive2 {
    /**
     * 单例模式 TCP核心管理模块
     * 功能：
     * 1.发送心跳保持连接确保不被服务器踢掉
     * 2.重连机制
     */
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final String TAG = "TCPCoreManger";
    private static TcpCoreManager instance = new TcpCoreManager();
    //private final static String TCP_SERVER="183.78.182.98";
    private final static String TCP_SERVER = "118.89.182.250";
    private final static int TCP_PORT = 9101;
    private final static int CONNECT_TIME_OUT = 11 * 1000;//毫秒
    private final static int HEART_RATE = 5000;//毫秒
    private final static int RECEIVE_TIME_OUT = 11 * 1000;//毫秒
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    private Socket sendSocket;
    private Timer heartTimer;
    OutputStream clientOS;
    InputStream clientIS;
    private String deviceID;
    private boolean createOnce = false;
    private boolean isListenserData=false;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    private Object lockObj = new Object();

    private TcpCoreManager() {
    }

    public static TcpCoreManager getInstance() {

        return instance;
    }

    public void closeConnect() {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (lockObj) {
                        createOnce = false;
                        isListenserData=false;
                        if (heartTimer != null) {
                            heartTimer.cancel();
                        }
                        if (sendSocket != null) {
                            sendSocket.close();
                            sendSocket = null;
                        }
                        if (clientOS != null) {
                            clientOS.close();
                        }
                        if (clientIS != null) {
                            clientIS.close();
                        }

                    }
                } catch (IOException e) {
                    System.err.println("closeConnect ...." + e);
                }
            }
        });


    }

    private void connect() {
        synchronized (lockObj) {
            isListenserData=true;
            if (!createOnce) {
                sendSocket = new Socket();
                System.err.println("create new socket...");
                createOnce = true;

            }
            if (sendSocket != null && !sendSocket.isConnected()) {
                SocketAddress socketAddress = new InetSocketAddress(TCP_SERVER, TCP_PORT);
                try {
                    sendSocket.connect(socketAddress, CONNECT_TIME_OUT);
                    sendSocket.setKeepAlive(true);
                    sendSocket.setOOBInline(true);
                    //sendSocket.setSoTimeout(RECEIVE_TIME_OUT);
                    clientOS = sendSocket.getOutputStream();
                    clientIS = sendSocket.getInputStream();
                    sendFuckHeart();
                } catch (IOException e) {
                    UIManager.getInstance().setChanged();
                    UIManager.getInstance().notifyDataObservers(ConstantUtils.ERROR_CODE_SCOKET_LOGIN_TIMEOUT);

                    closeConnect();
                }


            }

        }


    }


    public void sendData(final byte[] buffer) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                reciveLoop();

                connect();

                /**
                 * 发送socket 为空 则创建新的socket 启动接受数据
                 */
                if (sendSocket != null && sendSocket.isConnected()) {
                    try {
                        clientOS.write(buffer);
                        clientOS.flush();
                        System.err.println("send buffer....");
                    } catch (Exception e) {

                        UIManager.getInstance().setChanged();
                        UIManager.getInstance().notifyDataObservers(ConstantUtils.ERROR_CODE_SCOKET_COLOSE);

                        closeConnect();
                    }
                }
            }
        });

    }


    public void reciveLoop() {
        synchronized (lockObj) {
            if(!isListenserData){
                isListenserData=true;
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {

                            while (isListenserData) {
                                synchronized (lockObj) {
                                    if (sendSocket == null || sendSocket.isClosed() || clientIS == null) {
                                        continue;
                                    }
                                }
                                /**
                                 * 读取完整的包
                                 */
                                byte[] buffers = execute(clientIS);
                                if (buffers == null) {
                                    continue;
                                } else {
                                    onReadData(buffers, buffers.length);
                                }
                            }

                            if (!isListenserData) {
                                break;
                            }

                        }
                    }
                });

            }

        }



    }


    /**
     * @param contents
     * @param len      总长度
     */
    @Override
    public void onReadData(byte[] contents, int len) {


        SPackage thPackage = new SPackage(contents);


        System.err.println(" len:: " + len + "\n" + thPackage);



        /**
         * UIManager 发送消息给【ReceiveUI】界面
         */
        UIManager.getInstance().setChanged();
        UIManager.getInstance().notifyDataObservers(thPackage);

    }

    /**
     * 检测数据上报
     */
    public void checkData(String deviceID) {

        SPackage data1 = new SPackage(DeviceType.Android, deviceID,
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}
                , (short) 0x0003, (byte) 0x01, (short) 0);

        sendData(data1.getNeedSendDataPackages());

    }


    public void sendFuckHeart() {

        synchronized (lockObj) {
                if(heartTimer!=null){
                    heartTimer.cancel();
                }
                heartTimer = new Timer();
                heartTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                            /**
                             * 终端心跳
                             */
                            SPackage data1 = new SPackage(DeviceType.Android, deviceID,
                                    new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, (short) 0x0001, (byte) 0x00, (short) 0);

                            sendData(data1.getNeedSendDataPackages());

                            SPackage data0 = new SPackage(DeviceType.Water, deviceID,
                                    new byte[]{0x12, 0x78, (byte) 0xA0, (byte) 0x9C, 0x00, 0x00, 0x00, 0x00}, (short) 0x0002,
                                    (byte) 0x00, (short) 1);
                            data0.setContents(new byte[]{(byte) 0xFF});

                            sendData(data0.getNeedSendDataPackages());

                    }
                }, 200, 5000);
            }


    }

    /**
     * 解决粘包问题
     */
    private List<Byte> mBytes = new ArrayList<>();

    private void checkProtocal(int index,byte data){
        switch (index){
            case 0:
                if(data!=(byte) 0x2a){
                    UIManager.getInstance().setChanged();
                    UIManager.getInstance().notifyDataObservers(ConstantUtils.ERROR_CODE_ERROR_PROTOCAL);

                    closeConnect();
                }
                break;
            case 1:
                if(data != (byte) 0x54){
                    UIManager.getInstance().setChanged();
                    UIManager.getInstance().notifyDataObservers(ConstantUtils.ERROR_CODE_ERROR_PROTOCAL);

                    closeConnect();
                }
                break;
            case 2:
                if(data!=(byte) 0x5a){
                    UIManager.getInstance().setChanged();
                    UIManager.getInstance().notifyDataObservers(ConstantUtils.ERROR_CODE_ERROR_PROTOCAL);

                    closeConnect();
                }
                break;
        }

    }

    public byte[] execute(InputStream is) {
        if (is == null) {
            return null;
        }
        mBytes.clear();
        int lenStartIndex = 21;
        int lenEndIndex = 22;
        int offset = 26;
        byte[] lenField = new byte[2];
        int count = 0;
        int len = -1;
        byte temp;
        byte[] result;
        int msgLen = -1;
        try {
            while ((len = is.read()) != -1) {
                temp = (byte) len;
                checkProtocal(count,temp);

                if (count >= lenStartIndex && count <= lenEndIndex) {
                    lenField[count - lenStartIndex] = temp;//保存len字段
                    if (count == lenEndIndex) {//len字段保存结束，需要解析出来具体的长度了
                        msgLen = getLen(lenField);
                    }
                }
                count++;
                mBytes.add(temp);
                if (msgLen != -1) {//已结解析出来长度
                    if (count == msgLen + offset) {
                        break;
                    } else if (count > msgLen + offset) {//error
                        len = -2;//标记为error
                        break;
                    }
                }
            }
            if (len < 0) {
//				if(len==-1){
//					UIManager.getInstance().setChanged();
//					UIManager.getInstance().notifyDataObservers(ConstantUtils.ERROR_CODE_SCOKET_COLOSE);
//					closeConnect();
//				}
                return null;
            }
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                UIManager.getInstance().setChanged();
                UIManager.getInstance().notifyDataObservers(ConstantUtils.ERROR_CODE_SCOKET_READ_TIMEOUT);
                closeConnect();
            }
            return null;
        }
        result = new byte[mBytes.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = mBytes.get(i);
        }
        return result;
    }

    private int getLen(byte[] src) {
        int re = 0;
        for (byte b : src) {
            re = (re << 8) | (b & 0xff);
        }
        return re;
    }

}

interface IReceive2 {
    public void onReadData(byte[] contents, int len);
}
