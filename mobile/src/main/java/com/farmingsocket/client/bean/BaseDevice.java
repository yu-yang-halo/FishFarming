package com.farmingsocket.client.bean;

import java.util.List;
import java.util.Map;

public class BaseDevice {

	private String gprsmac;
	private String name;
	private List<Map<String, String>> switchs;
	private String mac;
	
	
	

	@Override
	public String toString() {
		return "BaseDevice [gprsmac=" + gprsmac + ", name=" + name + ", switchs=" + switchs + ", mac=" + mac + "]";
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

}
