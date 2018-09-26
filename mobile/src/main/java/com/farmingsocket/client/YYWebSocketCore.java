package com.farmingsocket.client;

import android.util.Log;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.fuck.fishfarming.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;

public class YYWebSocketCore {
	private static final YYWebSocketCore instance=new YYWebSocketCore();
	private static final String TAG = "YYWebSocketClient";
	private static final String HOSTNAME_WSS = "socket.tldwlw.com";
	private static final String HOSTNAME = BuildConfig.APP_IP_ADDRESS;
	private static final int PORT = 8080;
    private static final String HEAD_REQ= BuildConfig.APP_API;
    private static final boolean WSS_MODE = false;

	private WebSocketListener listenser;
	public void setListenser(WebSocketListener listenser) {
		this.listenser = listenser;
	}

	private YYWebSocketCore(){};
	
	public static YYWebSocketCore getInstance(){
		return instance;
	}

	private static SSLSocketFactory createSSLSocketFactory() {
		SSLSocketFactory sSLSocketFactory = null;
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[]{new TrustAllManager()},
					new SecureRandom());
			sSLSocketFactory = sc.getSocketFactory();
		} catch (Exception e) {
		}
		return sSLSocketFactory;
	}

	private static class TrustAllManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}

	private static class TrustAllHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	public void connect(String username,String password) {

		OkHttpClient client = new OkHttpClient
				.Builder()
//				.connectTimeout(1, TimeUnit.SECONDS)
//				.readTimeout(1, TimeUnit.SECONDS)
//				.writeTimeout(1, TimeUnit.SECONDS)
				.pingInterval(5, TimeUnit.SECONDS)
				.followSslRedirects(true)
				.hostnameVerifier(new TrustAllHostnameVerifier())
				.sslSocketFactory(createSSLSocketFactory())
				.build();

		String webSocketUrl = "wss://" + HOSTNAME  + "/"+HEAD_REQ+username+"/"+password;

		Request request = new Request.Builder().url(webSocketUrl).build();
		//Request request = new Request.Builder().url("wss://socket.tldwlw.com/tldservice/appwebsocket/guest/123456").build();
		client.newWebSocket(request, listenser);

		Log.v("webSocketUrl",webSocketUrl);

	}
}
