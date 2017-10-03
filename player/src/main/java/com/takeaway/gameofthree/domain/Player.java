package com.takeaway.gameofthree.domain;

public class Player {

	
	private Integer id;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
	
	public String getType(){
		return autonomous?"Autonomous":"Manual";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
