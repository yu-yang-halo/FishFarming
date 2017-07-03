package com.farmingsocket.manager;

public class ConstantsPool {
	/**
	 * 界面viewID
	 */
	public static final int VIEW_ID_REAL_DATA=0;
	public static final int VIEW_ID_REMOTE_CONTROL=0;


	public static final int ERROR_CODE_CONNECT_TIMEOUT=0x1001;
	public static final int ERROR_CODE_READ_TIMEOUT=0x1002;
	public static final int ERROR_CODE_CONNECT_OPEN=0x1003;
	public static final int ERROR_CODE_CONNECT_FAILURE=0x1004;
	public static final int ERROR_CODE_CONNECT_CLOSING=0x1005;
	public static final int ERROR_CODE_CONNECT_CLOSED=0x1006;


	
	
	public static enum  MethodType{
		GET,POST
	}

	public static final  byte AUTO_MODE= (byte) 0xAC;
	public static final  byte MANUAL_MODE= (byte) 0xDC;

}
