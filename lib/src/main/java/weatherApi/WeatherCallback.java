package weatherApi;

public interface WeatherCallback {
   public void onSuccess(String jsonData);
   public void onFail(String errorData);
}
