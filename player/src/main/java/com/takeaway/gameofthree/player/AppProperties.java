package com.takeaway.gameofthree.player;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("gameofthree")
public class AppProperties {

	private String serverIp;
	private int serverPort;
	private int divisionReference;
	private int valueToWin;
	
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public int getDivisionReference() {
		return divisionReference;
	}
	public void setDivisionReference(int divisionReference) {
		this.divisionReference = divisionReference;
	}
	public int getValueToWin() {
		return valueToWin;
	}
	public void setValueToWin(int valueToWin) {
		this.valueToWin = valueToWin;
	}
	
	
}
