package cn.netty.farmingsocket.data;

public interface ICmdPackageProtocol {
	/*
	 * 定义发送数据包
	 */
	public void sendFuckHeart();
	public void sendFuckControlCmd(int num,int cmdStatus,String deviceId,IDataCompleteCallback completeCallback);
	public void rangSetOrGet(MethodType type ,int max,int min,IDataCompleteCallback completeCallback);
	public void modeStatusSetOrGet(MethodType type,short mode,IDataCompleteCallback completeCallback);

	public void timeSetOrGet(MethodType type,short time,IDataCompleteCallback completeCallback);



	public void closeContext();
	public void registerDataCompleteCallback(IDataCompleteCallback callback,boolean tempYN);

	public void setDeviceId(String deviceId);




	public static enum  MethodType{
		GET,POST
	}

	public static final  byte AUTO_MODE= (byte) 0xAC;
	public static final  byte MANUAL_MODE= (byte) 0xDC;


}

