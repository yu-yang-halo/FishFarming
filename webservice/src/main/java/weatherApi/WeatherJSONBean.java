package weatherApi;

public class WeatherJSONBean {
	private String reason;
	private String error_code;
	private Result result;
	

	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	public static class Result{
		private WData data;

		public WData getData() {
			return data;
		}

		public void setData(WData data) {
			this.data = data;
		}

		@Override
		public String toString() {
			return "Result [data=" + data + "]";
		}

	}
	
	
	@Override
	public String toString() {
		return "WeatherJSONBean [reason=" + reason + ", error_code=" + error_code + ", result=" + result + "]";
	}
	public static class WData{
		private RealTime realtime;

		public RealTime getRealtime() {
			return realtime;
		}

		public void setRealtime(RealTime realtime) {
			this.realtime = realtime;
		}

		@Override
		public String toString() {
			return "WData [realtime=" + realtime + "]";
		}
		
	}
	public static class RealTime{
		private String city_code;
		private String city_name;
		private String date;
		private String time;
		private int week;
		private String moon;
		private WWeather weather;
		private WWind wind;
		public String getCity_code() {
			return city_code;
		}
		public void setCity_code(String city_code) {
			this.city_code = city_code;
		}
		public String getCity_name() {
			return city_name;
		}
		public void setCity_name(String city_name) {
			this.city_name = city_name;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public int getWeek() {
			return week;
		}
		public void setWeek(int week) {
			this.week = week;
		}
		public String getMoon() {
			return moon;
		}
		public void setMoon(String moon) {
			this.moon = moon;
		}
		public WWeather getWeather() {
			return weather;
		}
		public void setWeather(WWeather weather) {
			this.weather = weather;
		}
		public WWind getWind() {
			return wind;
		}
		public void setWind(WWind wind) {
			this.wind = wind;
		}
		@Override
		public String toString() {
			return "RealTime [city_code=" + city_code + ", city_name=" + city_name + ", date=" + date + ", time=" + time
					+ ", week=" + week + ", moon=" + moon + ", weather=" + weather + ", wind=" + wind + "]";
		}
		
		
		
	}
	
	public static class WWeather{
		private String  temperature;
		private String  humidity;
		private String  info;
		private String  img;
		public String getTemperature() {
			return temperature;
		}
		public void setTemperature(String temperature) {
			this.temperature = temperature;
		}
		public String getHumidity() {
			return humidity;
		}
		public void setHumidity(String humidity) {
			this.humidity = humidity;
		}
		public String getInfo() {
			return info;
		}
		public void setInfo(String info) {
			this.info = info;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		@Override
		public String toString() {
			return "WWeather [temperature=" + temperature + ", humidity=" + humidity + ", info=" + info + ", img=" + img
					+ "]";
		}
		
		
	}
    public static class WWind{
    	private String direct;
    	private String power;
		public String getDirect() {
			return direct;
		}
		public void setDirect(String direct) {
			this.direct = direct;
		}
		public String getPower() {
			return power;
		}
		public void setPower(String power) {
			this.power = power;
		}
		@Override
		public String toString() {
			return "WWind [direct=" + direct + ", power=" + power + "]";
		}
    	
    	
	}
}
/**

 ["{\"error_code\":\"0\",\"reason\":\"successed!\",\"result\":{\"data\":{\"realtime\":{\"city_code\":\"101090101\",\"city_name\":\"石家庄\",\"date\":\"2016-12-03\",\"moon\":\"十一月初五\",\"time\":\"16:00:00\",\"weather\":{\"humidity\":\"61\",\"img\":\"33\",\"info\":\"霾\",\"temperature\":\"8\"},\"wind\":{\"direct\":\"东风\",\"power\":\"2级\"},\"week\":6}}}}","{\"error_code\":\"0\",\"reason\":\"successed!\",\"result\":{\"data\":{\"realtime\":{\"city_code\":\"101090101\",\"city_name\":\"石家庄\",\"date\":\"2016-12-04\",\"moon\":\"十一月初六\",\"time\":\"09:00:00\",\"weather\":{\"humidity\":\"92\",\"img\":\"18\",\"info\":\"雾\",\"temperature\":\"1\"},\"wind\":{\"direct\":\"北风\",\"power\":\"1级\"},\"week\":0}}}}"]

 * */