package com.farmingsocket;

import com.farmingsocket.SPackage.DeviceType;
import com.farmingsocket.manager.ConstantsPool.MethodType;

public class TcpSocketService {
	private static TcpSocketService instance = null;
	private String deviceID;
	private TcpCoreManager tcpCoreManager = TcpCoreManager.getInstance();

	private TcpSocketService() {

	}

	public static TcpSocketService getInstance() {
		synchronized (TcpSocketService.class) {
			if (instance == null) {
				instance = new TcpSocketService();
			}
		}
		return instance;
	}



	/**
	 * 检测数据上报
	 */
	public void checkData() {
	
		SPackage data1 = new SPackage(DeviceType.Android, deviceID,
				new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }
		         , (short) 0x0003, (byte) 0x01, (short) 0);

		tcpCoreManager.sendData(data1.getNeedSendDataPackages());

	}

	/**
	 * 发送心跳 信息数据
	 */
	public void sendFuckHeart() {

		sendRegisterDevice();

		SPackage data0 = new SPackage(DeviceType.Water, deviceID,
				new byte[] { 0x12, 0x78, (byte) 0xA0, (byte) 0x9C, 0x00, 0x00, 0x00, 0x00 }, (short) 0x0002,
				(byte) 0x00, (short) 1);
		data0.setContents(new byte[] { (byte) 0xFF });

		tcpCoreManager.sendData(data0.getNeedSendDataPackages());
		
	
	}

	/**
	 * 发送注册 
	 */
	public void sendRegisterDevice() {
		SPackage data1 = new SPackage(DeviceType.Android, deviceID,
				new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, (short) 0x0001, (byte) 0x00, (short) 0);

		tcpCoreManager.sendData(data1.getNeedSendDataPackages());
	
	}
	

	public void sendFuckControlCmd(int num, int cmdStatus, String deviceId) {

		SPackage controlCmdData = new SPackage(DeviceType.Android, deviceID,
				new byte[] { 0x12, 0x78, (byte) 0xA0, (byte) 0x9C, 0x00, 0x00, 0x00, 0x00 }, (short) 0x0010,
				(byte) 0x02, (short) 0x0002);

		controlCmdData.setContents(new byte[] { (byte) (num & 0xFF), (byte) (cmdStatus & 0xFF) });

		tcpCoreManager.sendData(controlCmdData.getNeedSendDataPackages());
	}

	/**
	 * mode 0xAC表示自动模式，0xDC表示手动模式
	 * 
	 * @param type
	 * @param mode
	 */
	public void modeStatusSetOrGet(MethodType type, short mode) {

		SPackage controlCmdData;

		if (type == MethodType.GET) {
			controlCmdData = new SPackage(DeviceType.Android, deviceID, new byte[] { (byte) 0xFF, (byte) 0xFF,
					(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, (short) 21,
					(byte) 0x01, (short) 0);

		} else {
			controlCmdData = new SPackage(DeviceType.Android, deviceID, new byte[] { (byte) 0xFF, (byte) 0xFF,
					(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, (short) 21,
					(byte) 0x02, (short) 2);

			controlCmdData.setContents(new byte[] { (byte) 0x05, (byte) mode });

		}

		tcpCoreManager.sendData(controlCmdData.getNeedSendDataPackages());

	}

	public void rangSetOrGet(MethodType type, int max, int min) {

		SPackage controlCmdData;
		if (type == MethodType.GET) {
			controlCmdData = new SPackage(DeviceType.Android, deviceID,
					new byte[] { (byte)0xFF,(byte)0xFF,(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF },
					(short) 19,
					(byte) 0x01, (short) 0);

		} else {
			controlCmdData = new SPackage(DeviceType.Android, deviceID,
					new byte[] { (byte)0xFF,(byte)0xFF,(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF },
					(short) 19,
					(byte) 0x02, (short) 4);

			controlCmdData.setContents(new byte[]{(byte)0xAA,0x55, (byte) (max&0xFF), (byte) (min&0xFF)});

		}

		tcpCoreManager.sendData(controlCmdData.getNeedSendDataPackages());

	}

	public void timeSetOrGet(MethodType type, short time) {
		SPackage controlCmdData;
		if(type==MethodType.GET){
			 controlCmdData = new SPackage(DeviceType.Android, deviceID,
					new byte[] { (byte)0xFF,(byte)0xFF,(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF },
					(short) 24,
					(byte) 0x01, (short) 0);

		}else{
			 controlCmdData = new SPackage(DeviceType.Android, deviceID,
					new byte[] { (byte)0xFF,(byte)0xFF,(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF },
					(short) 24,
					(byte) 0x02, (short) 3);

			controlCmdData.setContents(new byte[]{(byte)0xA5,(byte)0x5A, (byte) time});

		}
		tcpCoreManager.sendData(controlCmdData.getNeedSendDataPackages());


	}
	public  void  closeConnect(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("TCP关闭连接....");
				tcpCoreManager.closeConnect();
			}
		}).start();

	}


	public void setDeviceId(String deviceId) {
		this.deviceID = deviceId;
		tcpCoreManager.setDeviceID(deviceId);
	}
}
