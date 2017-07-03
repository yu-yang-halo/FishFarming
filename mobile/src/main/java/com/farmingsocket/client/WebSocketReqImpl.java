package com.farmingsocket.client;

import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.farmingsocket.client.bean.BaseInfo;
import com.farmingsocket.helper.YYLogger;
import com.farmingsocket.manager.ConstantsPool;
import com.farmingsocket.manager.UIManager;
import com.google.gson.Gson;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketReqImpl extends AbstractWebSocketReqImpl {
	private static final WebSocketReqImpl instance = new WebSocketReqImpl();
	private static final String TAG = "WebSocketReqImpl";
	private YYWebSocketCore yySocketCore = YYWebSocketCore.getInstance();
	private WebSocket mWebSocket;
	private final ExecutorService writeExecutor = Executors.newCachedThreadPool();



	private WebSocketReqImpl() {
		yySocketCore.setListenser(this);
	}

	public static WebSocketReqImpl getInstance() {

		return instance;
	}

	@Override
	public void reqWebSocketData(final String json) {
		YYLogger.debug(TAG, "req ::: " + json);
		writeExecutor.execute(new Runnable() {

			@Override
			public void run() {
				if (mWebSocket != null) {
					mWebSocket.send(json);
				} else {
					YYLogger.debug(TAG, "mWebSocket is null ");
				}
			}
		});
		

	}

	@Override
	public void login(final String username, final String password) {
		writeExecutor.execute(new Runnable() {

			@Override
			public void run() {
				yySocketCore.connect(username, password);
			}
		});

	}

	@Override
	public void onOpen(WebSocket webSocket, Response response) {
		super.onOpen(webSocket, response);
		YYLogger.debug(TAG, "onOpen ....");
		mWebSocket = webSocket;

	}

	@Override
	public void onMessage(WebSocket webSocket, String text) {
		super.onMessage(webSocket, text);

		YYLogger.debug(TAG, "onMessage ...." + text);


		UIManager.getInstance().notifyDataObservers(text);

	}

	@Override
	public void onFailure(WebSocket webSocket, Throwable t, Response response) {
		super.onFailure(webSocket, t, response);

		YYLogger.debug(TAG, "onFailure ...." + t);

		if(t instanceof SocketTimeoutException){
			UIManager.getInstance().notifyDataObservers(ConstantsPool.ERROR_CODE_READ_TIMEOUT);
		}else {

		}



	}

	@Override
	public void onClosing(WebSocket webSocket, int code, String reason) {
		super.onClosing(webSocket, code, reason);
		YYLogger.debug(TAG, "onClosing ...." + reason);
		UIManager.getInstance().notifyDataObservers(ConstantsPool.ERROR_CODE_CONNECT_CLOSING);
	}

	@Override
	public void onClosed(WebSocket webSocket, int code, String reason) {
		super.onClosed(webSocket, code, reason);

		YYLogger.debug(TAG, "onClosed ...." + reason);

		UIManager.getInstance().notifyDataObservers(ConstantsPool.ERROR_CODE_CONNECT_CLOSED);

	}


}
