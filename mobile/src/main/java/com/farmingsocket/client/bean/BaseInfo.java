package com.farmingsocket.client.bean;

import java.util.List;

public class BaseInfo extends BaseCommand{
	

	private String dwmc;
	private String username;
	private List<BaseDevice>   device;


	@Override
	public String toString() {
		return "BaseInfo{" +
				"dwmc='" + dwmc + '\'' +
				", username='" + username + '\'' +
				", device=" + device +
				'}';
	}

	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<BaseDevice> getDevice() {
		return device;
	}
	public void setDevice(List<BaseDevice> device) {
		this.device = device;
	}
	
	
	
	
}
