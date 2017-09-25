package com.takeaway.gameofthree.player.util;

public class ThreadKiller extends Thread {

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
		
	}

	
	
}
