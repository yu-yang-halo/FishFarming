package com.farmingsocket.client.bean;

import java.util.List;

public class BaseInfo {
	

	private String dwmc;
	private String username;
	private List<BaseDevice>   device;
	private String command;
	private String errcode;
	
	
	
	@Override
	public String toString() {
		return "BaseInfo [dwmc=" + dwmc + ", username=" + username + ", device=" + device + ", command=" + command
				+ ", errcode=" + errcode + "]";
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
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	
	
	
	
}
