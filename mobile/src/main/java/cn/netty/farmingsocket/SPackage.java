package cn.netty.farmingsocket;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;

public class SPackage {
	public byte[] getContents() {
		return contents;
	}


	public void setContents(byte[] contents) {
		this.contents = contents;
	}
	
	public DeviceType getType() {
		return type;
	}


	public void setType(DeviceType type) {
		this.type = type;
	}

	/**
	 *             *TZ    
	 */
	 
	private byte  frameHeader;//帧头                          1byte            0   
	private short productinfo;//厂家信息                2bytes           1-2  
	private byte  version;//版本号                              1byte            3
	private short deviceType;//设备类型                   2bytes           4-5
	
	private DeviceType type;
	
	
	
	private int   deviceAddress;//设备地址           4bytes           6-9
	private String deviceID;
	private byte[]  cmdSerialNumber;//命令流水号   8bytes          10-17  
	
	private short cmdword;//命令字                              2bytes          18-19  
	
	private byte  flag ;//操作标识                               1byte             20
	private short length ;//数据包长度                       2bytes           21-22
	
	
	private byte[] contents;//23+ 23+length
	
	
	private short checkCode;//校验和                          2bytes        前所有字节的和 
	private short frameFooter;//帧尾                         1byte
	
	public SPackage(DeviceType type,String serialNo,
			byte[] cmdSerialNumber, short cmdword, byte flag, short length) {
		super();
		this.frameHeader = '*';
		this.productinfo = 21594;
		this.version = 2;
		this.type = type;
		this.deviceAddress =parseSerialNo(serialNo);
		this.cmdSerialNumber = cmdSerialNumber;
		this.cmdword = cmdword;
		this.flag = flag;
		this.length = length;
	}
	
	public SPackage(byte[] bytes){
		
		if(bytes.length>=26){
			this.frameHeader=bytes[0];
			this.productinfo=(short) ((bytes[1]<<8)+bytes[2]);
			this.version=bytes[3];
			this.deviceType=(short) ((bytes[4]<<8)+bytes[5]);
	
			this.deviceID=String.format("%02x-%02x-%02x-%02x",bytes[6],bytes[7],bytes[8],bytes[9]);
			
			this.cmdSerialNumber=new byte[8];
			System.arraycopy(bytes, 10, cmdSerialNumber, 0, 8);
			
			this.cmdword=(short) ((bytes[18]<<8)+bytes[19]);
			this.flag=bytes[20];
			this.length=(short) ((bytes[21]<<8)+bytes[22]);
			
			if(length>0){
				
				this.contents=new byte[length];
				
				System.arraycopy(bytes,23, contents, 0, length);
				
				
			}
			
			
			
			
			
		}
		logBytes("****************************接收数据*****:::",bytes);
	}
	
	private int parseSerialNo(String serialNo){
		String[] strArr=serialNo.split("-");
		int sValue=0;
		if(strArr.length!=4){
			return 0;
		}
		for(int i=0;i<strArr.length;i++){
			sValue+=Integer.parseInt(strArr[i],16)<<(8*(3-i));
		}
		
		return sValue;
		
	}
	
	public byte[] getNeedSendDataPackages(){
		//除去内容 数据包长度为26
		
		byte[] all=new byte[26+length];
		all[0]='*';
		all[1]='T';
		all[2]='Z';
		all[3]=2;//version
		if(type==DeviceType.Water){
			//水产
			all[4]=0x00;
			all[5]=(byte)0xFE;
		}else if(type==DeviceType.Android){
			all[4]=0x00;
			all[5]=(byte)0xFE;
		}
		
		
		for(int i=0;i<4;i++){
			
			all[6+i]= (byte) (deviceAddress>>8*(3-i)&0xFF);
			
		}
		
		for(int i=0;i<cmdSerialNumber.length;i++){
			all[10+i]=cmdSerialNumber[i];
		}
		
		for(int i=0;i<2;i++){
			all[18+i]=(byte) (cmdword<<8*(1-i)&0xFF);
		}
		all[20]=flag;
	
		for(int i=0;i<2;i++){
			all[21+i]=(byte) (length<<8*(1-i)&0xFF);
		}
		
		
		if(contents!=null){
			
			for(int i=0;i<length;i++){
				all[23+i]=contents[i];
			}
			
		}
		
		//具体数据长度
		short sum=0;
	    for(int i=0;i<23+length;i++){
	        sum+=all[i]&0xFF;
	    }
	    this.checkCode=sum; 
	    all[23+length]=(byte) ((sum>>8)&0xFF);
	    all[24+length]=(byte) (sum&0xFF);
	    
	    all[25+length]='#';
		
		
		
		
	    logBytes("***************************发送数据*****：：",all);
		
		return all;
	}
	
	
	private void logBytes(String tag,byte[] bytes){
		System.out.println(tag+"  ");
		if(bytes.length>=26){
			
			System.out.format("序号[0]帧头%02x\n",bytes[0]);
	
			System.out.format("序号[1-2]厂家信息%02x%02x\n",bytes[1],bytes[2]);
			System.out.format("序号[3]版本号%02x\n",bytes[3]);
			System.out.format("序号[4-5]设备类型%02x%02x\n",bytes[4],bytes[5]);
		    String customNo=String.format("%02x-%02x-%02x-%02x",bytes[6],bytes[7],bytes[8],bytes[9]);

		    System.out.format("序号[6-9]设备地址%02x%02x%02x%02x  customNO:%s\n",bytes[6],bytes[7],bytes[8],bytes[9],customNo);
		    
		    System.out.format("序号[10-17]命令流水号%02x%02x%02x%02x%02x%02x%02x%02x\n",bytes[10],bytes[11],bytes[12],bytes[13],bytes[14],bytes[15],bytes[16],bytes[17]);
		    //0003 3--内容  19--设置信息  15--电机控制状态
		    System.out.format("序号[18-19]操作命令字%02x%02x\n",bytes[18],bytes[19]);
		    
		    int controlCMD=(bytes[18]<<8)+bytes[19];
		    
		    System.out.format("序号[20]操作标志%02x\n",bytes[20]);
		    int len=(bytes[21]<<8)+bytes[22];
		    System.out.format("序号[21-22]数据包长度%02x%02x  len:%d\n",bytes[21],bytes[22],len);
		    System.out.println("数据内容:::::::::::");
		    for(int i=0;i<len;i++){
		    	System.out.format("%02x\n",bytes[23+i]);
		    }
		    System.out.println(":::::::::::::::::::::::::::");
		}
		
		System.out.println("*******************************************************");
		
	}
	
	
	
	
	
	
	public String getDeviceID() {
		return deviceID;
	}


	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}


	public byte getFrameHeader() {
		return frameHeader;
	}
	public void setFrameHeader(byte frameHeader) {
		this.frameHeader = frameHeader;
	}
	public short getProductinfo() {
		return productinfo;
	}
	public void setProductinfo(short productinfo) {
		this.productinfo = productinfo;
	}
	public byte getVersion() {
		return version;
	}
	public void setVersion(byte version) {
		this.version = version;
	}
	public short getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(short deviceType) {
		this.deviceType = deviceType;
	}
	public int getDeviceAddress() {
		return deviceAddress;
	}
	public void setDeviceAddress(int deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	public byte[] getCmdSerialNumber() {
		return cmdSerialNumber;
	}
	public void setCmdSerialNumber(byte[] cmdSerialNumber) {
		this.cmdSerialNumber = cmdSerialNumber;
	}
	public short getCmdword() {
		return cmdword;
	}
	public void setCmdword(short cmdword) {
		this.cmdword = cmdword;
	}
	public byte getFlag() {
		return flag;
	}
	public void setFlag(byte flag) {
		this.flag = flag;
	}
	public short getLength() {
		return length;
	}
	public void setLength(short length) {
		this.length = length;
	}
	public short getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(short checkCode) {
		this.checkCode = checkCode;
	}
	public short getFrameFooter() {
		return frameFooter;
	}
	public void setFrameFooter(short frameFooter) {
		this.frameFooter = frameFooter;
	}
	
	
	
	@Override
	public String toString() {
		return "SPackage [frameHeader=" + frameHeader + ", productinfo=" + productinfo + ", version=" + version
				+ ", deviceType=" + deviceType + ", type=" + type + ", deviceAddress=" + deviceAddress + ", deviceID="
				+ deviceID + ", cmdSerialNumber=" + Arrays.toString(cmdSerialNumber) + ", cmdword=" + cmdword
				+ ", flag=" + flag + ", length=" + length + ", contents=" + Arrays.toString(contents) + ", checkCode="
				+ checkCode + ", frameFooter=" + frameFooter + "]";
	}



	public static enum DeviceType{
		Android,Water;
	}
}

