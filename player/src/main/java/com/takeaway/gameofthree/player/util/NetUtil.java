package com.takeaway.gameofthree.player.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;

public class NetUtil {
	
	@Value("${gameofthree.server.ip}")
	public static final String SERVER_IP = "localhost";
	
	@Value("${gameofthree.server.port}")
	public static final int SERVER_PORT = 8080;
	
	public static String getLocalIP() {
		String retVal = "localhost";
		try {
			retVal = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	public static String getURLServer() {
		return "http://" + SERVER_IP + ":"
				+ SERVER_PORT + "server/api";
	}
	
	public static String getURLServer(String url) {
		return "http://" + SERVER_IP + ":"
				+ SERVER_PORT + "server/api"+url;
	}
}
