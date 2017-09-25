package com.takeaway.gameofthree.player.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.jboss.logging.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.domain.Status;
import com.takeaway.gameofthree.player.strategies.GameStrategy;
import com.takeaway.gameofthree.player.strategies.SmartStrategy;

@RestController
@RequestMapping("/api")
public class PlayerController {

	public static final String SERVER_IP = "localhost";
	public static final int SERVER_PORT = 8080;

	private GameStrategy strategy = new SmartStrategy();
	private RestTemplate restTemplate = new RestTemplate();

	private static String serverResponse;

	private static Player player;

	public PlayerController() {
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Player currentPlayer() {
		return player;
	}

	@RequestMapping(value = "/finalResult/{status}", method = RequestMethod.GET)
	public void receiveFinalResult(@PathVariable final Status status) {
		player.setStatus(status);
	}

	@RequestMapping(value = "/receive/{number}", method = RequestMethod.GET)
	public void receive(@PathVariable final int number) {
		System.out.println("Receiving number " + number);
		player.setCurrentNumber(number);
		player.setHaveNewValue(true);
		if (player.isAutonomous()) {
			int newNumber = strategy.executeStrategy(number);

			player.setCurrentNumber(newNumber);
			System.out.println("new number " + newNumber);

			if (newNumber == 1) {
				System.out.println("I won");
			}

			restTemplate.getForObject(getURLServer()
					+ "/play/{number}/player/{id}", String.class, newNumber,
					player.getId());
		}
	}
	
	@RequestMapping(value = "/manualPlay/{number}", method = RequestMethod.GET)
	public void manualPlay(@PathVariable final int number) {
		player.setCurrentNumber(number);
		restTemplate.getForObject(
				getURLServer() + "/play/{number}/player/{id}", String.class,
				player.getCurrentNumber(), player.getId());
	}

	@RequestMapping(value = "/askToStart", method = RequestMethod.GET)
	public void startGame() {
		System.out.println("try to start new game");
		serverResponse = restTemplate.getForObject(getURLServer() + "/start",
				String.class);
		System.out.println(serverResponse);
	}
	
	@RequestMapping(value = "/update/{autonomous}", method = RequestMethod.GET)
	public void updatePlayer(@PathVariable final boolean autonomous) {
		System.out.println("updating user to autonomous "+autonomous);
		player.setAutonomous(autonomous);
	}
	
	@RequestMapping(value = "/startNewValue", method = RequestMethod.GET)
	public void startNewValue() {
		System.out.println("starting new value");
		player.setHaveNewValue(false);
	}
	
	@RequestMapping(value = "/begin/{bound}", method = RequestMethod.GET)
	public void begin(@PathVariable final int bound) {
		int fisrtNumber = new Random().nextInt(bound);
		player.setCurrentNumber(fisrtNumber);
		System.out.println("First number " + fisrtNumber);
		serverResponse = restTemplate.getForObject(getURLServer()
				+ "/play/{number}/player/{id}", String.class, fisrtNumber,
				player.getId());
		System.out.println(serverResponse);

	}

	@RequestMapping(value = "/disconnect", method = RequestMethod.DELETE)
	public void disconect() {
		serverResponse = "Disconected!";
		player.setStatus(com.takeaway.gameofthree.domain.Status.DISCONNECTED);
		System.exit(0);
	}

	public static String getURLServer() {
		return "http://" + PlayerController.SERVER_IP + ":"
				+ PlayerController.SERVER_PORT + "server/api";
	}

	public static void registerPlayer(int port) {
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(
				PlayerController.getURLServer() + "/register/{ip}/{port}",
				String.class, getLocalIP(), port);

		if (response.startsWith("player")) {
			int id = Integer.valueOf(response.split(" ")[1]);

			player = new Player(id, getLocalIP(), port);
			serverResponse = "registred!";

		} else {
			serverResponse = response;
		}
	}

	private static String getLocalIP() {
		String retVal = "localhost";
		try {
			retVal = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return retVal;
	}
}
