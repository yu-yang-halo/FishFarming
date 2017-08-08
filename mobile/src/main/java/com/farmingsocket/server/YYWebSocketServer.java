//package com.farmingsocket.server;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import com.farmingsocket.client.bean.BaseDevice;
//import com.farmingsocket.client.bean.BaseHistData;
//import com.farmingsocket.client.bean.BaseInfo;
//import com.farmingsocket.client.bean.BaseOnlineData;
//import com.farmingsocket.client.bean.BaseRealTimeData;
//import com.farmingsocket.client.bean.BaseSwitchInfo;
//import com.farmingsocket.helper.JSONParseHelper;
//import com.google.gson.Gson;
//
//import cn.fuck.fishfarming.application.DataHelper;
//import cn.fuck.fishfarming.utils.ConstantUtils;
//import okhttp3.Response;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//
//public class YYWebSocketServer {
//	public static final String TAG = "YYWebSocketServer";
//	public static final YYWebSocketServer instance = new YYWebSocketServer();
//
//	private YYWebSocketServer() {
//
//	}
//
//	public static YYWebSocketServer getInstance() {
//		return instance;
//	}
//
//	private final MockWebServer mockWebServer = new MockWebServer();
//	private final ExecutorService writeExecutor = Executors.newSingleThreadExecutor();
//
//	private static BaseDevice createBaseDevice(int k) {
//		BaseDevice baseDevice = new BaseDevice();
//
//		baseDevice.setGprsmac("00000601");
//		baseDevice.setMac("0000060" + k);
//		baseDevice.setName("我家的养殖场"+k);
//
//		baseDevice.setOnline(1);
//
//		List<Map<String, String>> switchs = new ArrayList<>();
//		for (int i = 0; i < 8; i++) {
//			Map<String, String> dict = new HashMap<>();
//			dict.put("0" + i, "开关" + i);
//			switchs.add(dict);
//		}
//
//		baseDevice.setSwitchs(switchs);
//
//		return baseDevice;
//	}
//
//	private static BaseInfo createBaseInfo() {
//		BaseInfo baseInfo = new BaseInfo();
//
//		baseInfo.setCommand(100);
//		baseInfo.setErrcode(0);
//		baseInfo.setDwmc("太湖");
//		baseInfo.setUsername("yuyang");
//
//		List<BaseDevice> baseDevices = new ArrayList<>();
//
//		baseDevices.add(createBaseDevice(1));
//		baseDevices.add(createBaseDevice(2));
//
//		baseInfo.setDevice(baseDevices);
//		return baseInfo;
//	}
//
//	private static BaseRealTimeData createRealTimeData(String mac,int k){
//		BaseRealTimeData baseRealTimeData=new BaseRealTimeData();
//
//		baseRealTimeData.setErrcode(0);
//		baseRealTimeData.setCommand(102);
//		baseRealTimeData.setMac(mac);
//
//
//		baseRealTimeData.setRealData(createRealDataMap(k));
//
//
//		return baseRealTimeData;
//	}
//
//	private static Map createRealDataMap(int k){
//        Map dict=new HashMap<>();
//
//		dict.put("time", "2017-7-9 "+k+":33:33");
//		dict.put("a1", 12+Math.random()*30);
//		dict.put("a3", 7+Math.random()*10);
//		dict.put("a4", 1+Math.random()*10);
//		dict.put("a5", 24+Math.random()*60);
//
//		return dict;
//	}
//
//	private static BaseHistData createHistData(String mac){
//		BaseHistData baseHistData=new BaseHistData();
//
//		baseHistData.setCommand(104);
//		baseHistData.setErrcode(0);
//		baseHistData.setMac(mac);
//
//		List<Map> histDatas=new ArrayList<>();
//
//		for(int i=0;i<20;i++){
//			histDatas.add(createRealDataMap(i));
//		}
//
//		baseHistData.setHistDatas(histDatas);
//
//		return baseHistData;
//	}
//
//	private static BaseSwitchInfo createBaseSwitchInfo(String mac,String no,int val){
//		BaseSwitchInfo baseSwitchInfo=new BaseSwitchInfo();
//
//		baseSwitchInfo.setCommand(101);
//		baseSwitchInfo.setErrcode(0);
//		baseSwitchInfo.setMac(mac);
//		Map<String,String> switchs=new HashMap<>();
//
//		for (int i = 0; i < 8; i++) {
//			String number="0"+i;
//			if(number.equals(no)){
//				switchs.put(number,"0"+val);
//			}else{
//				switchs.put(number,"0"+i%2);
//			}
//		}
//
//		baseSwitchInfo.setSwitchs(switchs);
//
//		return baseSwitchInfo;
//	}
//
//	private static BaseSwitchInfo createBaseSwitchInfo(String mac){
//		BaseSwitchInfo baseSwitchInfo=new BaseSwitchInfo();
//
//		baseSwitchInfo.setCommand(101);
//		baseSwitchInfo.setErrcode(0);
//		baseSwitchInfo.setMac(mac);
//		Map<String, String> switchs = new HashMap<>();
//		for (int i = 0; i < 8; i++) {
//
//			switchs.put("0" + i, "0" + i%2);
//
//		}
//
//		baseSwitchInfo.setSwitchs(switchs);
//
//		return baseSwitchInfo;
//	}
//	private static BaseOnlineData createBaseOnlineData(){
//		BaseOnlineData baseOnlineData=new BaseOnlineData();
//
//		baseOnlineData.setCommand(109);
//		baseOnlineData.setErrcode(0);
//
//
//		List<BaseDevice> baseDevices = new ArrayList<>();
//
//		baseDevices.add(createBaseDevice(1));
//		baseDevices.add(createBaseDevice(2));
//		baseOnlineData.setDevice(baseDevices);
//
//		return baseOnlineData;
//	}
//
//	public void startServer() {
//
//
//		String hostName = mockWebServer.getHostName();
//		int port = mockWebServer.getPort();
//
//		System.out.println("hostName:" + hostName);
//		System.out.println("port:" + port);
//
//		DataHelper.getMyApp().setPort(port);
//
//
//		mockWebServer.enqueue(new MockResponse().withWebSocketUpgrade(new WebSocketListener() {
//
//			@Override
//			public void onOpen(final WebSocket webSocket, Response response) {
//				super.onOpen(webSocket, response);
//
//
//
//				writeExecutor.execute(new Runnable() {
//
//					@Override
//					public void run() {
//						BaseInfo baseInfo = createBaseInfo();
//
//						Gson gson = new Gson();
//						String json = gson.toJson(baseInfo);
//
//						webSocket.send(json);
//
//					}
//				});
//
//			}
//
//			@Override
//			public void onMessage(final WebSocket webSocket, final String text) {
//				super.onMessage(webSocket, text);
//
//
//
//				writeExecutor.execute(new Runnable() {
//
//					@Override
//					public void run() {
//						Gson gson = new Gson();
//						Map dict = gson.fromJson(text, Map.class);
//
//						int command = JSONParseHelper.objectToInt(dict.get("command"));
//						String mac=dict.get("mac")==null?null:dict.get("mac").toString();
//
//						switch (command) {
//						case 102:
//							//实时数据
//
//							String realDataJSON=gson.toJson(createRealTimeData(mac,10));
//							webSocket.send(realDataJSON);
//
//							break;
//
//						case 104:
//							// 历史数据
//							String histDataJSON=gson.toJson(createHistData(mac));
//							webSocket.send(histDataJSON);
//
//							break;
//						case 101:
//							Map kv= (Map) dict.get("switch");
//							// 设备开关状态
//							String switchInfoJSON2=gson.toJson(createBaseSwitchInfo(mac,kv.get("no").toString(),JSONParseHelper.objectToInt(kv.get("val"))));
//							webSocket.send(switchInfoJSON2);
//							break;
//						case 105:
//							// 设备开关状态
//							String switchInfoJSON=gson.toJson(createBaseSwitchInfo(mac));
//							webSocket.send(switchInfoJSON);
//							break;
//						case 109:
//							// 设备在线状态
//							String onlineJSON=gson.toJson(createBaseOnlineData());
//							webSocket.send(onlineJSON);
//							break;
//						}
//
//					}
//				});
//
//			}
//
//			@Override
//			public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//				super.onFailure(webSocket, t, response);
//
//
//
//			}
//
//			@Override
//			public void onClosing(WebSocket webSocket, int code, String reason) {
//				super.onClosing(webSocket, code, reason);
//
//
//			}
//
//			@Override
//			public void onClosed(WebSocket webSocket, int code, String reason) {
//				super.onClosed(webSocket, code, reason);
//
//
//
//			}
//
//		}));
//
//	}
//
//}
