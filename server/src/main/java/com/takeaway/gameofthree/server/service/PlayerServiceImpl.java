package com.takeaway.gameofthree.server.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.domain.Status;
import com.takeaway.gameofthree.server.AppProperties;
import com.takeaway.gameofthree.server.exception.GameException;
import com.takeaway.gameofthree.server.exception.GameOverException;
import com.takeaway.gameofthree.util.Generator;

public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private AppProperties properties;

	@Autowired
	private RestTemplate restTemplate;

	private List<Player> queue;
	
	private Iterator<Player> iteratorGame;

	public static final Logger logger = LoggerFactory
			.getLogger(PlayerServiceImpl.class);

	@PostConstruct
	private void init() {
		queue = new ArrayList<>();

	}

	public List<Player> findAll() {
		return queue;
	}

	public Player add(Player player) throws GameException {
		if (iteratorGame != null) {
			throw new GameException(
					"The game has started, new players are not allowed!");
		} else if (isGameFull()) {
			throw new GameException("Number of players exceed, Only "
					+ properties.getMaxUsers() + " users are allowed!");
		} else {
			if (player.getId() == null) {
				player.setId(Generator.getId());
				queue.add(player);
			}

			if (isGameReadyToStart()) {
				for (Player storedPlayer : queue) {
					storedPlayer.setStatus(Status.READY);
				}
			}
		}

		return player;
	}

	public void remove(Player player) {
		Iterator<Player> itPlayer = queue.iterator();
		Player playerLocal = null;
		while (itPlayer.hasNext()) {
			playerLocal = itPlayer.next();
			if (playerLocal.getId() == player.getId()) {
				itPlayer.remove();
				break;
			}
		}

		restTemplate.delete(playerLocal.getUrl());

		if (!isGameReadyToStart()) {
			sincronizePlayers(Status.WAITING);
		}

	}

	public void play(Player player) throws GameException {
		if (player.getCurrentNumber() < 1) {
			throw new GameException(
					"The number is invalid, please choose another one");
		} else if (!isGameReadyToStart()) {
			throw new GameException(
					"There is not sufficient players available, wait for your opponent(s)!");
		} else if (iteratorGame == null) {
			throw new GameException(
					"Illegal move, the game has not started, you have to request the start!");
		}

		logger.info("Receiving new value " + player.getCurrentNumber()
				+ " from player " + player.getId());

		update(player);

		simulateDelay();

		if (player.getCurrentNumber() == 1) {
			iteratorGame = null;
			declareFinalResult(player.getId());
			throw new GameOverException("Game is Over, Player "
					+ player.getId() + " wins!");
		}

		Player nextPlayer = null;

		if (!iteratorGame.hasNext()) {
			nextPlayer = renewRound();
		} else {
			nextPlayer = iteratorGame.next();
		}

		logger.info("Sending " + player.getCurrentNumber() + " to player "
				+ nextPlayer.getId() + " from player " + player.getId());

		nextPlayer.setCurrentNumber(player.getCurrentNumber());

		update(nextPlayer);

		final String nextUrl = nextPlayer.getUrl();

		new Thread() {
			public void run() {
				restTemplate.postForObject(nextUrl + "/receive/", player,
						Player.class);
			}
		}.start();
	}

	public Player update(Player player) {
		for (Player tempPlayer : queue) {
			if (tempPlayer.equals(player)) {
				tempPlayer.setCurrentNumber(player.getCurrentNumber());
				tempPlayer.setAutonomous(player.isAutonomous());
				tempPlayer.setHaveNewValue(player.isHaveNewValue());
				tempPlayer.setIp(player.getIp());
				tempPlayer.setPort(player.getPort());
				tempPlayer.setStatus(player.getStatus());
				break;
			}
		}
		return player;
	}

	public Player findById(int id) {
		Player retVal = null;
		for (Player playerQueue : queue) {
			if (playerQueue.getId() == id) {
				retVal = playerQueue;
				break;
			}
		}
		return retVal;
	}

	public void startGame(int idPlayer) throws GameException {
		logger.info("Fetching & startingUser with id {}", idPlayer);

		if (!isGameReadyToStart()) {
			throw new GameException(
					"The minimum amount of players is not enough to start");
		}

		sincronizePlayers(Status.PLAYING, 0);
		setGamePosition(idPlayer);

		Player nextPlayer = renewRound();

		new Thread() {
			public void run() {
				restTemplate.postForObject(nextPlayer.getUrl()
						+ "/game/{bound}/begin", properties.getLimitValue(),
						Integer.class, properties.getLimitValue());
			}
		}.start();

	}

	private void sincronizePlayers(Status status) {
		List<Player> queue = findAll();
		for (Player player : queue) {
			player.setStatus(status);
			restTemplate.postForObject(player.getUrl(), player, Player.class);
		}
	}

	private void sincronizePlayers(Status status, int initialNumber) {
		List<Player> queue = findAll();
		for (Player player : queue) {
			player.setStatus(status);
			player.setCurrentNumber(0);
			restTemplate.postForObject(player.getUrl(), player, Player.class);
		}
	}

	private void simulateDelay() {
		try {
			Thread.sleep(properties.getPlayDelay());
		} catch (InterruptedException e) {
			logger.error("Eror during sleeping", e);
		}
	}

	private void declareFinalResult(int idWinner) {
		List<Player> queue = findAll();
		for (Player player : queue) {
			if (player.getId() == idWinner) {
				player.setStatus(Status.WINNER);
			} else {
				player.setStatus(Status.LOSER);
			}
			restTemplate.postForObject(player.getUrl(), player, Player.class);
		}
	}

	private Player setGamePosition(int idPlayer) {
		Player player = null;
		iteratorGame = queue.iterator();
		while (iteratorGame.hasNext()) {
			player = iteratorGame.next();
			if (player.getId() == idPlayer) {
				break;
			}
		}
		return player;
	}

	private Player renewRound() {
		iteratorGame = queue.iterator();
		return iteratorGame.next();
	}

	private boolean isGameReadyToStart() {
		return queue.size() >= properties.getMinUsers();
	}

	private boolean isGameFull() {
		return queue.size() == properties.getMaxUsers();
	}

}
