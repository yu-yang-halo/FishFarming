package cn.netty.farmingsocket;


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
import java.util.logging.Logger;


public class TcpCoreManager implements IReceive2{
	/**
	 * 单例模式 TCP核心管理模块
	 * 功能：
	 * 1.发送心跳保持连接确保不被服务器踢掉
	 * 2.重连机制
	 */
	private static final String TAG="TCPCoreManger";
	private static TcpCoreManager instance = new TcpCoreManager();
	private final static String TCP_SERVER="183.78.182.98";
	//private final static String TCP_SERVER="192.168.11.193";
	private final static int TCP_PORT = 9101;
	private final static int CONNECT_TIME_OUT=2500;//毫秒
	private final static int HEART_RATE=5000;//毫秒
	private final static int RECEIVE_TIME_OUT=11*1000;//毫秒
	private Socket sendSocket;
	private boolean isListenserData=true;
	private Timer heartTimer;
	OutputStream clientOS;
	InputStream clientIS;

	private Object lockObj=new Object();

	private TcpCoreManager() {
		reciveLoop();
	}
	public static TcpCoreManager getInstance() {

		return instance;
	}
	public void closeConnect(){

		isListenserData=false;
		if(heartTimer!=null){
			heartTimer.cancel();
		}
		try {
			synchronized (lockObj){
				if(sendSocket!=null){
					sendSocket.close();
					sendSocket=null;
				}
				if(clientOS!=null){
					clientOS.close();
				}
				if(clientIS!=null){
					clientIS.close();
				}

			}
		} catch (IOException e) {
			System.err.println("closeConnect ...."+e);
		}
		
	}

	private void connect(){
		synchronized (lockObj){
			if(sendSocket==null){
				sendSocket = new Socket();
				System.err.println("create new socket...");
			}
			if(sendSocket!=null&&!sendSocket.isConnected()){
				SocketAddress socketAddress = new InetSocketAddress(TCP_SERVER,TCP_PORT);
				try {
					sendSocket.connect(socketAddress, CONNECT_TIME_OUT);
					sendSocket.setKeepAlive(true);
					sendSocket.setOOBInline(true);
					sendSocket.setSoTimeout(RECEIVE_TIME_OUT);
					clientOS=sendSocket.getOutputStream();
					clientIS=sendSocket.getInputStream();
				} catch (IOException e) {
					sendSocket = new Socket();
					
				}
			}

		}



	}




	public void sendData(final byte[] buffer) {


		isListenserData=true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				connect();
				/**
				 * 发送socket 为空 则创建新的socket 启动接受数据
				 */

				 if(sendSocket!=null&&sendSocket.isConnected()){
					 try {
						 clientOS.write(buffer);
						 clientOS.flush();
						 System.err.println("send buffer....");
					 } catch (Exception e) {
						 System.err.println("send error ..."+e);
						 closeConnect();
					 }
				 }
			}
		}).start();

	}



	public  void reciveLoop(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				    while (true){

					while(isListenserData){
						if(sendSocket==null){
							continue;
						}
						if(sendSocket.isClosed()){

							continue;
						}

							if(clientIS==null){
								continue;
							}

							/**
                             * 读取完整的包
							 */
							byte[] buffers=execute(clientIS);
							if(buffers==null){
								continue;
							}else{

								onReadData(buffers,buffers.length);
							}
					}

					}
			}
		}).start();

	}


	/**
	 *
	 * @param contents
	 * @param len         总长度
     */
	@Override
	public void onReadData(byte[] contents,int len) {


		SPackage thPackage=new SPackage(contents);


		System.err.println(" len:: "+len+"\n"+thPackage);

		/**
		 * UIManager 发送消息给【BaseUI】界面
		 */



	}

	/**
	 * 解决粘包问题
	 */
	private List<Byte> mBytes=new ArrayList<>();
	public byte[] execute(InputStream is) {
		mBytes.clear();
		int lenStartIndex=21;
		int lenEndIndex=22;
		int offset=26;
		byte[] lenField = new byte[2];
		int count = 0;
		int len = -1;
		byte temp;
		byte[] result;
		int msgLen = -1;
		try {
			while ((len = is.read()) != -1) {
				temp = (byte) len;
				if (count >= lenStartIndex && count <= lenEndIndex) {
					lenField[count - lenStartIndex] = temp;//保存len字段
					if (count == lenEndIndex) {//len字段保存结束，需要解析出来具体的长度了
						msgLen =getLen(lenField);
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
				if(len==-1){
					closeConnect();
				}
				return null;
			}
		} catch (IOException e) {
			if(e instanceof SocketTimeoutException){
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
interface IReceive2{
	public void onReadData(byte[] contents,int len);
}
