package com.farmingsocket.client;

import okhttp3.WebSocketListener;

public abstract class AbstractWebSocketReqImpl  extends WebSocketListener implements IWebSocketReq {
	

	/**
	 * 发送心跳
	 * command 0
	 * {"command":0}
	 */
	public void sendHeart(){
		String reqJSON = "{ \"command\": 0 }";

		reqWebSocketData(reqJSON);
	}
	

	/**
	 * 控制设备开关 command 101 {"mac": ,"gprsmac": ,"command":101,"switch":{"no":
	 * ,"val": }}
	 */
	public void controlDevice(String mac, String gprsmac, String no, String val) {

		String reqJSON = "{\"mac\": " + mac + " ,\"gprsmac\":" + gprsmac + " ,\"command\":101,\"switch\":{\"no\": " + no
				+ " ,\"val\":" + val + " }}";

		reqWebSocketData(reqJSON);

	}

	/**
	 * 获取实时数据 command 102 {"mac": , "command":102 }
	 */
	public void fetchRealTimeData(String mac) {

		String reqJSON = "{\"mac\":" + mac + " , \"command\": 102 }";

		reqWebSocketData(reqJSON);

	}

	/**
	 * 断开实时数据推送 command 103 {"mac": , "command": 103 }
	 */
	public void stopRealTimeData(String mac) {
		String reqJSON = "{\"mac\":" + mac + " , \"command\": 103 }";

		reqWebSocketData(reqJSON);
	}

	/**
	 * 获取历史数据 command 104 {"mac" , "gprsmac": ,"command":104 ,"day": } day 0:今天
	 * 1:昨天 2:前天
	 */
	public void fetchHistoryData(String mac, String gprsmac, int day) {

		String reqJSON = "{\"mac\": " + mac + " , \"gprsmac\": " + gprsmac + ",\"command\":104 ,\"day\": " + day + "}";

		reqWebSocketData(reqJSON);

	}

	/**
	 * 获取设备的开关状态 command 105 {"mac" , "gprsmac": ,"command": 105 }
	 *
	 */
	public void fetchDeviceStatus(String mac, String gprsmac) {
		String reqJSON = "{\"mac\": " + mac + " , \"gprsmac\": " + gprsmac + ",\"command\":105 }";

		reqWebSocketData(reqJSON);
	}

	/**
	 * 获取风险指数测量与亚硝酸盐测量状态 command 106 {"mac" , "gprsmac": ,"command": 106 }
	 */

	public void fetchRiskIndexAndNitriteStatus(String mac, String gprsmac) {
		String reqJSON = "{\"mac\": " + mac + " , \"gprsmac\": " + gprsmac + ",\"command\":106 }";

		reqWebSocketData(reqJSON);
	}

	/**
	 * 风险指数测量 或者 亚硝酸盐测量 command 107 which 1 ：风险指数 2： 测量亚硝酸盐含量 {"mac" ,
	 * "gprsmac": ,"command": 107, "which":1 }
	 * 
	 */

	public void mesureRiskOrNitrite(String mac, String gprsmac, int which) {
		String reqJSON = "{\"mac\": " + mac + " , \"gprsmac\": " + gprsmac + ",\"command\":107 ,\"which\":" + which
				+ "}";

		reqWebSocketData(reqJSON);
	}

	/**
	 * 获取设备在线状态
	 * command 109
	 *
	 */
	public void fetchDeviceOnlineStatus(){
		String reqJSON = "{\"command\": 109 }";

		reqWebSocketData(reqJSON);
	}

	/**
	 * 断开所有实时数据推送 command 108 {"command": }
	 */
	public void stopAllRealTimeData() {
		String reqJSON = "{\"command\": 108 }";

		reqWebSocketData(reqJSON);
	}
	
	

}
