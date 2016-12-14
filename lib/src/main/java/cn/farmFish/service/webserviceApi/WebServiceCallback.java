package cn.farmFish.service.webserviceApi;

public interface WebServiceCallback {
   public void onSuccess(String jsonData);
   public void onFail(String errorData);
}
