package com.takeaway.gameofthree.domain;

public class Player {

	private int id;
	
	private Status status;
	
	private int currentNumber;
	
	private String ip;
	
	private int port;
	
	private boolean autonomous;
	
	private boolean haveNewValue;
	
	public Player() {
	}
	
	public Player(int id, String ip, int port) {
		this.id = id;
		this.status = Status.READY;
		this.ip = ip;
		this.port = port;
		this.autonomous = true;
		this.haveNewValue = false;
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getCurrentNumber() {
		return currentNumber;
	}

	public void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String getUrl(){
		return "http://" + this.ip + ":"
				+ this.port+"/player/api";
	}
	
	public boolean isAutonomous() {
		return autonomous;
	}
	
	public boolean getAutonomous() {
		return autonomous;
	}

	public void setAutonomous(boolean autonomous) {
		this.autonomous = autonomous;
	}

	public boolean isHaveNewValue() {
		return haveNewValue;
	}

	public void setHaveNewValue(boolean haveNewValue) {
		this.haveNewValue = haveNewValue;
	}
}
