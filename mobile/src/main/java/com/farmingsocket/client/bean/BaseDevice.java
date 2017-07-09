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

	private Map<String,String> stringSwitchgMap;


	public Map<String, String> getStringSwitchMap() {
		stringSwitchgMap=new HashMap<>();

		if(switchs!=null){

			for(Map dict:switchs){
				Iterator<Map.Entry<String,String>> entryIterator=dict.entrySet().iterator();

				while (entryIterator.hasNext()) {
					Map.Entry<String, String> entry = entryIterator.next();
					stringSwitchgMap.put(entry.getKey(), entry.getValue());
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
}
