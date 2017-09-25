package com.takeaway.gameofthree.player.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

@Component
public class NetUtil {
	
	public static String getLocalIP() {
		String retVal = "localhost";
		try {
			retVal = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
}
