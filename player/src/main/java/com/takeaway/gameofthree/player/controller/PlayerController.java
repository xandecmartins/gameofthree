package com.takeaway.gameofthree.player.controller;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

	//private static String serverResponse;

	private Player player;

	public PlayerController() {
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Player getPlayer() {
		return player;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void updtaePlayer(@RequestBody Player player) {
		this.player = player;
	}

	@RequestMapping(value = "/receive", method = RequestMethod.POST)
	public void receive(@RequestBody Player player) {
		logger.info("Receiving number " + player.getCurrentNumber());
		this.player = player;
		player.setHaveNewValue(true);
		if (player.isAutonomous()) {
			int newNumber = strategy.executeStrategy(player.getCurrentNumber());
			player.setCurrentNumber(newNumber);
			logger.info("new number " + newNumber);

			if (newNumber == properties.getValueToWin()) {
				logger.info("I won");
			}

			restTemplate.postForObject(getURLServer("/players/{id}/play"),
					player, Integer.class, player.getId());
		}
	}

	@RequestMapping(value = "/manualPlay/{number}", method = RequestMethod.POST)
	public void manualPlay(@PathVariable final int number) {
		player.setCurrentNumber(number);
		restTemplate.getForObject(getURLServer("/play/{number}/player/{id}"),
				String.class, player.getCurrentNumber(), player.getId());
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public void startGame() {
		logger.info("try to start new game");
		restTemplate.getForObject(getURLServer("/start"),
				String.class);
	}

	@RequestMapping(value = "/update/{autonomous}", method = RequestMethod.POST)
	public void updatePlayer(@PathVariable final boolean autonomous) {
		logger.info("updating user to autonomous " + autonomous);
		player.setAutonomous(autonomous);
	}

	@RequestMapping(value = "/startNewValue", method = RequestMethod.POST)
	public void startNewValue() {
		logger.info("starting new value");
		player.setHaveNewValue(false);
	}

	@RequestMapping(value = "/game/{bound}/begin", method = RequestMethod.POST)
	public void begin(@PathVariable final int bound) {
		int fisrtNumber = new Random().nextInt(bound);
		player.setCurrentNumber(fisrtNumber);
		logger.info("First number " + fisrtNumber);
		restTemplate.postForObject(
				getURLServer("/players/{id}/play"), player, Player.class, player.getId());
	}

	@RequestMapping(value = "/disconnect", method = RequestMethod.DELETE)
	public void disconect() {
		new ThreadKiller().start();
	}

	public void registerPlayer(int port) {
		player = new Player();
		player.setIp(NetUtil.getLocalIP());
		player.setPort(port);
		player.setStatus(Status.WAITING);
		player.setAutonomous(true);
		player = restTemplate.postForObject(getURLServer("players"), player,
				Player.class);
		logger.info("player registred... ID: " + player.getId());
	}

	public String getURLServer() {
		return "http://" + properties.getServerIp() + ":"
				+ properties.getServerPort() + "server/api";
	}

	public String getURLServer(String url) {
		return "http://" + properties.getServerIp() + ":"
				+ properties.getServerPort() + "/server/api/" + url;
	}

}
