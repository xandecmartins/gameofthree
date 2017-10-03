package com.takeaway.gameofthree.player.controller;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.takeaway.gameofthree.domain.CustomErrorType;
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
	public ResponseEntity<?> receive(@RequestBody Player playerRemote) {
		logger.info("Receiving number " + playerRemote.getCurrentNumber());
		player.setCurrentNumber(playerRemote.getCurrentNumber());
		player.setStatus(playerRemote.getStatus());
		player.setHaveNewValue(true);
		if (player.isAutonomous()) {
			int newNumber = strategy.executeStrategy(player.getCurrentNumber());
			player.setCurrentNumber(newNumber);
			logger.info("new number " + newNumber);

			if (newNumber == properties.getValueToWin()) {
				logger.info("I won");
			}

			new Thread() {
				public void run() {
					restTemplate.postForObject(
							getURLServer("/players/{id}/play"), player,
							Player.class, player.getId());
				}
			}.start();
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{number}/manual_play", method = RequestMethod.POST)
	public ResponseEntity<?> manualPlay(@PathVariable final int number) {
		if (player.isAutonomous()) {
			logger.error("Ilegal move, the player is configured as autonomous");
			return new ResponseEntity<CustomErrorType>(new CustomErrorType(
					"Ilegal move, the player is configured as autonomous"),
					HttpStatus.BAD_REQUEST);
		} else if (number < 1) {
			logger.error("The value " + number
					+ "is invalid, choose another one");
			return new ResponseEntity<CustomErrorType>(new CustomErrorType(
					"The valeu " + number + " is invalid, choose another one"),
					HttpStatus.BAD_REQUEST);
		}
		player.setCurrentNumber(number);

		new Thread() {
			public void run() {
				restTemplate.postForObject(getURLServer("/players/{id}/play"),
						player, Player.class, player.getId());
			}
		}.start();

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public ResponseEntity<?> startGame() {
		logger.info("try to start new game");
		try {
			restTemplate.postForObject(getURLServer("/players/{id}/start"),
					player, String.class, player.getId());
		} catch (Exception e) {
			logger.error("Error while registring player");
			return new ResponseEntity<CustomErrorType>(new CustomErrorType(
					"The amount of users is not enough to start"),
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{autonomous}/change", method = RequestMethod.POST)
	public ResponseEntity<?> updatePlayer(@PathVariable final boolean autonomous) {
		logger.info("switching user to autonomous " + autonomous);
		player.setAutonomous(autonomous);
		restTemplate.put(getURLServer("players/{id}"), player, player.getId());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/mark_new_value", method = RequestMethod.POST)
	public ResponseEntity<?> startNewValue() {
		logger.info("receiving new value");
		player.setHaveNewValue(false);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/game/{bound}/begin", method = RequestMethod.POST)
	public ResponseEntity<?> begin(@PathVariable final int bound) {
		int fisrtNumber = new Random().nextInt(bound);
		player.setCurrentNumber(fisrtNumber);
		logger.info("First number " + fisrtNumber);
		new Thread() {
			public void run() {
				restTemplate.postForObject(getURLServer("/players/{id}/play"),
						player, Player.class, player.getId());
			}
		}.start();

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public ResponseEntity<?> disconect() {
		new ThreadKiller().start();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public void registerPlayer(int port) {
		player = new Player();
		player.setIp(NetUtil.getLocalIP());
		player.setPort(port);
		player.setStatus(Status.WAITING);
		player.setAutonomous(true);
		try {
			player = restTemplate.postForObject(getURLServer("players"),
					player, Player.class);
			logger.info("player registred... ID: " + player.getId());
		} catch (Exception e) {
			logger.error("Error while registring player", e);
			new ThreadKiller().start();
		}

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
