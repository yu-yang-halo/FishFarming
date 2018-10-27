package com.farmingsocket.client;

public interface IWebSocketReq {
	/**
	 * 登录
	 */
	
	public void login(String username,String password);

	public void login(String username,String password,String serverAddress);


	/**
	 * 登出
	 */
	public void logout();

	
	/**
	 * 发送心跳
	 * command 0
	 * {"command":0}
	 */
	public void sendHeart();
	
	
	
	/**
	 * 控制设备开关
	 * command 101
	 * {"mac": ,"gprsmac": ,"command":101,"switch":{"no": ,"val": }}
	 */
	public void controlDevice(String mac,String gprsmac,String no,int val);
	
	/**
	 * 获取实时数据
	 * command 102
	 * {"mac": , "command": }
	 */
	public void fetchRealTimeData(String mac);
	
	/**
	 * 断开实时数据推送
	 * command 103
	 * {"mac": , "command": }
	 */
	public void stopRealTimeData(String mac);
	
	
	/**
	 * 获取历史数据
	 * command 104
	 * {"mac" , "gprsmac": ,"command": ,"day": }
	 * day 0:今天   1:昨天   2:前天
	 */
	public void fetchHistoryData(String mac,String gprsmac,int day);
	
	
	/**
	 * 获取设备的开关状态
	 * command 105
	 * {"mac" , "gprsmac": ,"command":  }
	 *
	 */
	public void fetchDeviceStatus(String mac,String gprsmac);
	
	/**
	 * 获取风险指数测量与亚硝酸盐测量状态
	 * command 106
	 */
	
	public void fetchRiskIndexAndNitriteStatus(String mac,String gprsmac);
	
	/**
	 * 风险指数测量 或者 亚硝酸盐测量
	 * command 107
	 * which 1 ：风险指数      2： 测量亚硝酸盐含量
	 */
	
	public void mesureRiskOrNitrite(String mac,String gprsmac,int which);


	/**
	 * 获取设备在线状态
	 * command 109
	 *
	 */
	public void fetchDeviceOnlineStatus();
	

	/**
	 * 断开所有实时数据推送
	 * command 108
	 * {"command": }
	 */
	public void stopAllRealTimeData();

	/**
	 * 设置阈值
	 * 	command 110
     */
	public void configThreshold(String mac,String gprsmac,int min,int max);

	/**
	 * 获取阈值
	 * command 111
     */
	public void fetchThreshold(String mac,String gprsmac);

	/**
	 * 设置模式： 自动，手动，时间
	 * 1：自动、2：手动、3：时段控制模式
	 * command 113
     */
	public void configMode(String mac,String gprsmac,int mode);

	/**
	 * 获取模式
	 * command 114
     */
	public void fetchMode(String mac,String gprsmac);

	/**
	 * 设置增氧机自动开启间隔
	 * command 119
     */
	public void configPeriod(String mac,int min);

	/**
	 * 获取增氧机自动开启间隔
	 * command 120
     */
	public void fetchPeriod(String mac);




	public void reqWebSocketData(String json);
	
	
	

}
