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

	public static final int COMMAND_ERROR=-1;
	public static final int COMMAND_HEART=0;
	public static final int COMMAND_LOGIN_INFO=100;
	public static final int COMMAND_SIWTCH_CONTROL_INFO=101;
	public static final int COMMAND_REAL_TIME_DATA=102;
	public static final int COMMAND_HISTORY_DATA=104;
	public static final int COMMAND_RISKINDEX_NS=106;
	public static final int COMMAND_ONLINE_STATUS=109;
	public static final int COMMAND_THRESHOLD=111;
	public static final int COMMAND_MODE=114;
	public static final int COMMAND_PERIOD=120;

	
	public static enum  MethodType{
		GET,POST
	}
    //1：自动、2：手动、3：时段控制模式
	public static final  int MODE_AUTO= 1;
	public static final  int MODE_MANUAL=2;
	public static final  int MODE_TIME_CONTROL=3;
}
