package com.farmingsocket.client.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseDevice {

	private String gprsmac;
	private String name;
	@SerializedName("switchs")
	private List<Map<String, String>> switchs;
	private String mac;
	private int online;
	private List<Sense> sensors;

	private Map<String,String> stringSwitchgMap;


	public Map<String, String> getStringSwitchMap() {
		stringSwitchgMap=new HashMap<>();

		if(switchs!=null){

			for(Map<String,String> dict:switchs){
				String no=dict.get("no");
				String name=  dict.get("name");
				if(no!=null&&name!=null){
					stringSwitchgMap.put(no, name);
				}

			}

		}

		return stringSwitchgMap;
	}

	@Override
	public String toString() {
		return "BaseDevice{" +
				"gprsmac='" + gprsmac + '\'' +
				", name='" + name + '\'' +
				", switchs=" + switchs +
				", mac='" + mac + '\'' +
				", online=" + online +
				'}';
	}

	public String getGprsmac() {
		return gprsmac;
	}

	public void setGprsmac(String gprsmac) {
		this.gprsmac = gprsmac;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<Map<String, String>> getSwitchs() {
		return switchs;
	}

	public void setSwitchs(List<Map<String, String>> switchs) {
		this.switchs = switchs;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public List<Sense> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sense> sensors) {
		this.sensors = sensors;
	}

	public static class Sense{
		private float min;
		private float max;
		private String no;
		private String danwei;
		private String name;
		private float   y;
		private String lx;

		public float getMin() {
			return min;
		}

		public void setMin(float min) {
			this.min = min;
		}

		public float getMax() {
			return max;
		}

		public void setMax(float max) {
			this.max = max;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public String getDanwei() {
			return danwei;
		}

		public void setDanwei(String danwei) {
			this.danwei = danwei;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}

		public String getLx() {
			return lx;
		}

		public void setLx(String lx) {
			this.lx = lx;
		}
	}

}
