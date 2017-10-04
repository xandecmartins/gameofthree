package com.takeaway.gameofthree.player.util;

public class ThreadKiller extends Thread {

	private long time;
	
	public ThreadKiller(int time) {
		this.time=time;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
		
	}

	
	
}
