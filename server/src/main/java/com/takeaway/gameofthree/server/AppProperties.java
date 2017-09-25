package com.takeaway.gameofthree.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("gameofthree")
public class AppProperties {
	
	private int minUsers;
	private int maxUsers;
	private int limitValue;
	private int playDelay;
	
	public int getMinUsers() {
		return minUsers;
	}
	public void setMinUsers(int minUsers) {
		this.minUsers = minUsers;
	}
	public int getMaxUsers() {
		return maxUsers;
	}
	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}
	public int getLimitValue() {
		return limitValue;
	}
	public void setLimitValue(int limitValue) {
		this.limitValue = limitValue;
	}
	public int getPlayDelay() {
		return playDelay;
	}
	public void setPlayDelay(int playDelay) {
		this.playDelay = playDelay;
	}
}
