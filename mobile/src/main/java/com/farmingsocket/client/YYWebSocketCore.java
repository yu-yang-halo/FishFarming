package com.farmingsocket.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import cn.fuck.fishfarming.application.DataHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class YYWebSocketCore {
	private static final YYWebSocketCore instance=new YYWebSocketCore();
	private static final String TAG = "YYWebSocketClient";
	private static final String HOSTNAME = "127.0.0.1";
	private static  int PORT = 54929;
	//private static final String HOSTNAME = "118.89.182.250";
	//private static final int PORT = 8080;
    //private static final String HEAD_REQ="tldservice/appwebsocket/";
	private static final String HEAD_REQ="";



	private WebSocketListener listenser;
	
	
	public void setListenser(WebSocketListener listenser) {
		this.listenser = listenser;
	}

	private YYWebSocketCore(){};
	
	public static YYWebSocketCore getInstance(){
		return instance;
	}

	public void connect(String username,String password) {
		
		OkHttpClient client = new OkHttpClient
				.Builder()
//				.connectTimeout(1, TimeUnit.SECONDS)
//				.readTimeout(1, TimeUnit.SECONDS)
//				.writeTimeout(1, TimeUnit.SECONDS)
				.pingInterval(5, TimeUnit.SECONDS)
				
				.build();
		
		PORT=	DataHelper.getMyApp().getPort();
		//Request request = new Request.Builder().url("ws://" + HOSTNAME + ":" + PORT + "/"+HEAD_REQ+username+"/"+password).build();
		Request request = new Request.Builder().url("ws://" + HOSTNAME + ":" + PORT + "/").build();
		client.newWebSocket(request, listenser);

		
		

	}

}
