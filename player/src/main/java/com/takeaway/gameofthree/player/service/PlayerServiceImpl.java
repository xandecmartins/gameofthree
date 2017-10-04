package com.takeaway.gameofthree.player.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.domain.Status;
import com.takeaway.gameofthree.player.AppProperties;
import com.takeaway.gameofthree.player.exception.IllegalMoveException;
import com.takeaway.gameofthree.player.strategies.GameStrategy;
import com.takeaway.gameofthree.player.util.NetUtil;
import com.takeaway.gameofthree.player.util.ThreadKiller;

public class PlayerServiceImpl implements PlayerService {

	public static final Logger logger = LoggerFactory
			.getLogger(PlayerServiceImpl.class);

	@Autowired
	private GameStrategy strategy;

	@Autowired
	private AppProperties properties;

	@Autowired
	private RestTemplate restTemplate;

	private Player player;

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public void update(Player player) {
		this.player = player;

	}

	public void play(Player playerRemote) {
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
	}

	public void manualPlay(int number) throws IllegalMoveException {
		if (player.isAutonomous()) {
			throw new IllegalMoveException(
					"Ilegal move, the player is configured as autonomous");
		} else if (number < 1) {
			throw new IllegalMoveException("The valeu " + number
					+ " is invalid, choose another one");
		}

		player.setCurrentNumber(number);

		restTemplate.postForObject(getURLServer("/players/{id}/play"), player,
				Player.class, player.getId());
	}

	public void syncWithServer() {
		restTemplate.put(getURLServer("players/{id}"), player, player.getId());
	}

	public void askToStart() {
		logger.info("try to start new game");
		restTemplate.postForObject(getURLServer("/players/{id}/start"), player,
				String.class, player.getId());
	}

	public void begin(int bound) {
		int fisrtNumber = new Random().nextInt(bound);
		player.setCurrentNumber(fisrtNumber);
		logger.info("First number " + fisrtNumber);

		restTemplate.postForObject(getURLServer("/players/{id}/play"), player,
				Player.class, player.getId());

	}

	public void disconnect() {
		new ThreadKiller(1000).start();
	}

	public void register(int port) {
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
			new ThreadKiller(1000).start();
		}

	}
	
	private String getURLServer(String url) {
		return "http://" + properties.getServerIp() + ":"
				+ properties.getServerPort() + "/server/api/" + url;
	}

}
