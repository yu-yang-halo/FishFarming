package weatherApi;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WeatherApi {
	private static WeatherApi instance = null;

	private static String REQUEST_URL="http://op.juhe.cn/onebox/weather/query";
	private static String juheKEY="7621836ff352deeee8c88dd07c60ca1e";
	
	private WeatherApi() {

	}

	public static WeatherApi getInstance() {
		synchronized (WeatherApi.class) {
			if (instance == null) {
				instance = new WeatherApi();
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		WeatherApi.getInstance().getWeatherDataByCity("合肥",new WeatherCallback() {
			
			@Override
			public void onSuccess(String jsonData) {
				// TODO Auto-generated method stub
				System.out.println(jsonData);
				

				
			}
			
			@Override
			public void onFail(String errorData) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	   
	 
	public void getWeatherDataByCity(String cityName,WeatherCallback block){
		 String urlString=REQUEST_URL+"?cityname="+cityName+"&dtype=&key="+juheKEY;
		 okHttpRequest(urlString,"",block);
	}
	
	private void okHttpRequest(String urlString, String postBody, final WeatherCallback block) {
		OkHttpClient mOkHttpClient = new OkHttpClient();
	
		final Request request = new Request.Builder()
				                    .url(urlString)
				                    .addHeader("content-type", "application/json;charset:utf-8")
				                    .post(RequestBody.create(  
				                            MediaType.parse("application/json; charset=utf-8"),  
				                            postBody))
				                    .build();
		Call call = mOkHttpClient.newCall(request);
		call.enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				System.err.println("exception : " + arg1);
				block.onFail(arg1.toString());
			}

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				
				String retJSON=fetechNetWorkData(arg1.body().byteStream());
				block.onSuccess(retJSON);
				
				
			}
		});
	}
	
	private  String fetechNetWorkData(InputStream is) {
		StringBuffer sb = null;
		try {
			sb = new StringBuffer();

			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = is.read(buffer)) > 0) {

				sb.append(new String(buffer, 0, i,"utf-8"));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sb.toString();

	}


}
