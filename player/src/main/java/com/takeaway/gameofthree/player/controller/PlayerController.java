package com.takeaway.gameofthree.player.controller;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.domain.Status;
import com.takeaway.gameofthree.player.AppProperties;
import com.takeaway.gameofthree.player.strategies.GameStrategy;
import com.takeaway.gameofthree.player.util.NetUtil;
import com.takeaway.gameofthree.player.util.ThreadKiller;

@RestController
@RequestMapping("/api")
public class PlayerController {

	public static final Logger logger = LoggerFactory
			.getLogger(PlayerController.class);

	@Autowired
	private GameStrategy strategy;

	@Autowired
	private AppProperties properties;

	@Autowired
	private RestTemplate restTemplate;

	private static String serverResponse;

	private static Player player;

	public PlayerController() {
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Player currentPlayer() {
		return player;
	}

	@RequestMapping(value = "/newStatus/{status}", method = RequestMethod.GET)
	public void receiveFinalResult(@PathVariable final Status status) {
		player.setStatus(status);
	}

	@RequestMapping(value = "/newStatus/{status}/currentNumber/{currentNumber}", method = RequestMethod.GET)
	public void receiveFinalResult(@PathVariable final Status status,
			@PathVariable final int currentNumber) {
		player.setStatus(status);
		player.setCurrentNumber(currentNumber);
	}

	@RequestMapping(value = "/receive/{number}", method = RequestMethod.GET)
	public void receive(@PathVariable final int number) {
		logger.info("Receiving number " + number);
		player.setCurrentNumber(number);
		player.setHaveNewValue(true);
		if (player.isAutonomous()) {
			int newNumber = strategy.executeStrategy(number);

			player.setCurrentNumber(newNumber);
			logger.info("new number " + newNumber);

			if (newNumber == properties.getValueToWin()) {
				logger.info("I won");
			}

			restTemplate.getForObject(
					getURLServer("/play/{number}/player/{id}"), String.class,
					newNumber, player.getId());
		}
	}

	@RequestMapping(value = "/manualPlay/{number}", method = RequestMethod.GET)
	public void manualPlay(@PathVariable final int number) {
		player.setCurrentNumber(number);
		restTemplate.getForObject(getURLServer("/play/{number}/player/{id}"),
				String.class, player.getCurrentNumber(), player.getId());
	}

	@RequestMapping(value = "/askToStart", method = RequestMethod.GET)
	public void startGame() {
		logger.info("try to start new game");
		serverResponse = restTemplate.getForObject(getURLServer("/start"),
				String.class);
		logger.info(serverResponse);
	}

	@RequestMapping(value = "/update/{autonomous}", method = RequestMethod.GET)
	public void updatePlayer(@PathVariable final boolean autonomous) {
		logger.info("updating user to autonomous " + autonomous);
		player.setAutonomous(autonomous);
	}

	@RequestMapping(value = "/startNewValue", method = RequestMethod.GET)
	public void startNewValue() {
		logger.info("starting new value");
		player.setHaveNewValue(false);
	}

	@RequestMapping(value = "/begin/{bound}", method = RequestMethod.GET)
	public void begin(@PathVariable final int bound) {
		int fisrtNumber = new Random().nextInt(bound);
		player.setCurrentNumber(fisrtNumber);
		logger.info("First number " + fisrtNumber);
		serverResponse = restTemplate.getForObject(
				getURLServer("/play/{number}/player/{id}"), String.class,
				fisrtNumber, player.getId());
		logger.info(serverResponse);

	}

	@RequestMapping(value = "/disconnect", method = RequestMethod.DELETE)
	public void disconect() {
		new ThreadKiller().start();
	}

	public void registerPlayer(int port) {
		RestTemplate restTemplate = new RestTemplate();
		player = restTemplate.getForObject(
				getURLServer("/register/{ip}/{port}"), Player.class,
				NetUtil.getLocalIP(), port);
	}

	public String getURLServer() {
		return "http://" + properties.getServerIp() + ":"
				+ properties.getServerPort() + "server/api";
	}

	public String getURLServer(String url) {
		return "http://" + properties.getServerIp() + ":"
				+ properties.getServerPort() + "server/api" + url;
	}

}
