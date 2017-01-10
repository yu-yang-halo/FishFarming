package cn.farmFish.service.webserviceApi;

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

public class WebServiceApi {
	private static WebServiceApi instance = null;

	private static String REQUEST_URL="http://183.78.182.98:9110/service.svc/";


	private static String REQUEST_ALERT_URL="http://183.78.182.98:9005/AppService/AppHandler.ashx";




	private WebServiceApi() {

	}

	public static WebServiceApi getInstance() {
		synchronized (WebServiceApi.class) {
			if (instance == null) {
				instance = new WebServiceApi();
			}
		}
		return instance;
	}

	private void okHttpRequest(String urlString, String postBody, final WebServiceCallback block) {
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

	/**
	 * 用户登录
	 */
	public void Login(String username,String password,WebServiceCallback block) {
		String urlString=REQUEST_URL+"Login";
		String postBody = "{\"userAccount\":\""+username+"\",\"userPwd\":\""+password+"\"}";
		okHttpRequest(urlString,postBody,block);
	}
	/**
	 * 获取采集设备信息
	 */
	public void GetCollectorInfo(String userAccount,String customerNo,WebServiceCallback block){
		String urlString=REQUEST_URL+"GetCollectorInfo";
		String postBody = "{\"userAccount\":\""+userAccount+"\",\"customerNo\":\""+customerNo+"\"}";
		okHttpRequest(urlString,postBody,block);
	}
	/**
	 * 获取采集实时数据 历史数据
	 */
	public void GetCollectorData(String collectorId,String dateTime,WebServiceCallback block){
		String urlString=REQUEST_URL+"GetCollectorData";
		String postBody = "{\"collectorId\":\""+collectorId+"\",\"dateTime\":\""+dateTime+"\"}";
		okHttpRequest(urlString,postBody,block);
	}
	/**
	 * 根据场地编号、设备编号，获取视频信息
	 */
	public void GetVideoInfo(String filedId,String collectorId,WebServiceCallback block){
		String urlString=REQUEST_URL+"GetVideoInfo";
		String postBody = "{\"filedId\":\""+filedId+"\",\"collectorId\":\""+collectorId+"\"}";
		okHttpRequest(urlString,postBody,block);
	}

	/**
	 * 根据账户名获取视频信息
	 */
	public void GetUserVideoInfo(String userAccount,WebServiceCallback block){
		String urlString=REQUEST_URL+"GetUserVideoInfo";
		String postBody = "{\"userAccount\":\""+userAccount+"\"}";
		okHttpRequest(urlString,postBody,block);
	}


	/**
	 * categoryId  类型---->  1:新闻    2:知识库
	 * num
	 */
	public void GetNewsList(int categoryId,int num,WebServiceCallback block){
		String urlString=REQUEST_URL+"GetNewsList";
		String postBody = "{\"categoryId\":\""+categoryId+"\",\"num\":\""+num+"\"}";
		okHttpRequest(urlString,postBody,block);
	}

	/**
	 *
	 *  CmdID 固定值：GetWarningList
	 *  CompanyID      公司ID
	 *  CollectorID    采集器ID
	 *  BeginDateTime  起始时间
	 *  EndDateTime    结束时间
     */
	public void GetWarningList(String companyId,String collectorId,String startTime,String endTime,WebServiceCallback block){

		String urlString=REQUEST_ALERT_URL;
		String postBody = "{\"CmdID\":\"GetWarningList\",\"CompanyID\":"+companyId+",\"CollectorID\":"+collectorId+",\"BeginDateTime\":"+startTime+",\"EndDateTime\":"+endTime+"}";

		okHttpRequest(urlString,postBody,block);

	}

	/**
	 *
	 *  CmdID          GetCollecotSensorList
	 *  CollectorID    采集器ID
	 *  SensorID       传感器ID
	 *  CollectType    采集类别
     */

	public  void GetCollecotSensorList(String collectionId,String sensorId,int collectType,WebServiceCallback block){
		String urlString=REQUEST_ALERT_URL;
		String postBody = "{\"CmdID\":\"GetCollecotSensorList\",\"CollectorID\":\""+collectionId+"\",\"SensorID\":\""+sensorId+"\",\"CollectType\":\""
				+collectType+"\"}";

		okHttpRequest(urlString,postBody,block);
	}


	/**
	 *   CmdID            SetCollectorSensor
	 *   CollectorID      采集器ID
	 *   SensorID         传感器ID
	 *   ParamID          参数id，对应“获取采集器下传感器的参数列表”接口中，数据合计某行的F_ID字段
	 *   LowerValue       区间最小值
	 *   UpperValue       区间最大值
	 *   IsWarning        是否告警，1为是，0为否
	 *
     */
	public  void SetCollectorSensor(String collectionId,String sensorId,int paramId,float LowerValue,
									float UpperValue,short IsWarning,WebServiceCallback block){
		String urlString=REQUEST_ALERT_URL;
		String postBody = "{\"CmdID\":\"SetCollectorSensor\",\"CollectorID\":"+collectionId+",\"SensorID\":"+sensorId
				+",\"ParamID\":"+paramId+",\"LowerValue\":"+LowerValue+",\"UpperValue\":"+UpperValue+",\"IsWarning\":"+IsWarning+"}";
		okHttpRequest(urlString,postBody,block);
	}

	public static void main(String[] args) {
//		WebServiceApi.getInstance().Login("cjlsc", "cjlsc",new WebServiceCallback() {
//			@Override
//			public void onSuccess(String jsonData) {
//				// TODO Auto-generated method stub
//				System.err.println("onSuccess result::::::"+jsonData);
//			}
//			@Override
//			public void onFail(String errorData) {
//				// TODO Auto-generated method stub
//				System.err.println("onFail result::::::"+errorData);
//			}
//		});
//
//		WebServiceApi.getInstance().GetCollectorInfo("cjlsc", "00-00-04-01",new WebServiceCallback() {
//			@Override
//			public void onSuccess(String jsonData) {
//				// TODO Auto-generated method stub
//				System.err.println("onSuccess result::::::"+jsonData);
//			}
//			@Override
//			public void onFail(String errorData) {
//				// TODO Auto-generated method stub
//				System.err.println("onFail result::::::"+errorData);
//			}
//		});
		WebServiceApi.getInstance().GetCollecotSensorList("68eeffe7-9561-4a0f-9a7d-751c4cca98fe", "1", 1, new WebServiceCallback() {
			@Override
			public void onSuccess(String jsonData) {
				System.err.println("onSuccess result::::::"+jsonData);
			}

			@Override
			public void onFail(String errorData) {

			}
		});
//		WebServiceApi.getInstance().GetCollectorData("00-00-04-01", "2016-10-08",new WebServiceCallback() {
//			@Override
//			public void onSuccess(String jsonData) {
//				// TODO Auto-generated method stub
//				System.err.println("onSuccess result::::::"+jsonData);
//			}
//			@Override
//			public void onFail(String errorData) {
//				// TODO Auto-generated method stub
//				System.err.println("onFail result::::::"+errorData);
//			}
//		});

//		WebServiceApi.getInstance().GetUserVideoInfo("cjlsc",new WebServiceCallback() {
//			@Override
//			public void onSuccess(String jsonData) {
//				// TODO Auto-generated method stub
//				System.err.println("onSuccess result::::::"+jsonData);
//			}
//			@Override
//			public void onFail(String errorData) {
//				// TODO Auto-generated method stub
//				System.err.println("onFail result::::::"+errorData);
//			}
//		});
//		WebServiceApi.getInstance().GetNewsList(2,20,new WebServiceCallback() {
//			@Override
//			public void onSuccess(String jsonData) {
//				// TODO Auto-generated method stub
//				System.err.println("onSuccess result::::::"+jsonData);
//			}
//			@Override
//			public void onFail(String errorData) {
//				// TODO Auto-generated method stub
//				System.err.println("onFail result::::::"+errorData);
//			}
//		});


	}

}
