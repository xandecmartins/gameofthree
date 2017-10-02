package com.takeaway.gameofthree.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.domain.Status;
import com.takeaway.gameofthree.server.AppProperties;
import com.takeaway.gameofthree.server.service.PlayerService;

@RestController
@RequestMapping("/api")
public class ServerController {

	public static final Logger logger = LoggerFactory
			.getLogger(ServerController.class);

	@Autowired
	private AppProperties properties;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PlayerService playerService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/players/", method = RequestMethod.GET)
	public ResponseEntity<List<Player>> listAllPlayers() {
		List<Player> queue = playerService.findAll();
		if (queue.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Player>>(queue, HttpStatus.OK);
	}

	@RequestMapping(value = "/players/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable("id") int id) {
		logger.info("Fetching Player with id {}", id);
		return new ResponseEntity<Player>(playerService.findById(id),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/players/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePlayer(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting User with id {}", id);

		Player player = playerService.findById(id);
		playerService.remove(id);

		restTemplate.delete(player.getUrl());
		
		sincronizePlayers();

		return new ResponseEntity<String>(HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/players/{id}/start", method = RequestMethod.POST)
	public ResponseEntity<?> startGameWithPlayer(@PathVariable("id") int id) {
		logger.info("Fetching & startingUser with id {}", id);
		if (!playerService.isGameReadyToStart()) {
			logger.info("The amount of users is not enough to start");
			return new ResponseEntity<String>(
					"The amount of users is not enough to start",
					HttpStatus.BAD_REQUEST);
		}

		renewRemoteStatus(Status.PLAYING);

		Player player = playerService.startGame(id);

		restTemplate.postForObject(player.getUrl() + "/game/{bound}/begin",
				properties.getLimitValue(), Integer.class, properties.getLimitValue());
		return new ResponseEntity<String>("The was started with sucess!",
				HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/players", method = RequestMethod.POST)
	public ResponseEntity<?> register(@RequestBody Player player) {
		logger.debug("Receiving new player..." + player.getIp() + " - "
				+ player.getPort());

		if (playerService.isGameStarted()) {
			HashMap<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message",
					"The game has started, new players are not allowed!");
			return new ResponseEntity<Map<String, Object>>(errorResponse,
					HttpStatus.BAD_REQUEST);
		} else if (playerService.isGameFull()) {
			HashMap<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message", "Number of players exceed");
			return new ResponseEntity<Map<String, Object>>(errorResponse,
					HttpStatus.BAD_REQUEST);
		} else {
			playerService.add(player);
			sincronizePlayers();
			return new ResponseEntity<Player>(player, HttpStatus.CREATED);
		}

	}

	@RequestMapping(value = "/game/start", method = RequestMethod.POST)
	public ResponseEntity<?> startGame() {
		logger.info("starting new game");
		if (!playerService.isGameReadyToStart()) {
			logger.info("The amount of users is not enough to start");
			HashMap<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message",
					"The amount of users is not enough to start");
			return new ResponseEntity<Map<String, Object>>(errorResponse,
					HttpStatus.BAD_REQUEST);
		}

		renewRemoteStatus(Status.PLAYING);

		Player player = playerService.startGame();
		restTemplate.getForObject(player.getUrl() + "/begin/{bound}",
				Integer.class, properties.getLimitValue());

		return new ResponseEntity<Player>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/players/{id}/play", method = RequestMethod.POST)
	public ResponseEntity<?> play(@PathVariable final int id,
			@RequestBody Player player) {

		if (!playerService.isGameReadyToStart()) {
			logger.info("There is not sufficient players available, wait for your opponent(s)!");
			HashMap<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse
					.put("message",
							"There is not sufficient players available, wait for your opponent(s)!");
			return new ResponseEntity<Map<String, Object>>(errorResponse,
					HttpStatus.BAD_REQUEST);
		}
		logger.info("Receiving new value " + player.getCurrentNumber());

		simulateDelay();

		playerService.update(player);

		if (player.getCurrentNumber() == 1) {
			playerService.setGameStarted(Boolean.FALSE);
			declareFinalResult(id);
			logger.info("Game is over!");
			return new ResponseEntity<String>(HttpStatus.ACCEPTED);
		}

		Player nextPlayer = null;

		if (playerService.verifyRoundIsFinished()) {
			nextPlayer = playerService.renewRound();
		} else {
			nextPlayer = playerService.getNextPlayer();
		}

		logger.info("Sending " + player.getCurrentNumber() + " to player "
				+ nextPlayer.getId());
		
		nextPlayer.setCurrentNumber(player.getCurrentNumber());
		
		playerService.update(nextPlayer);
		
		final String nextUrl = nextPlayer.getUrl();
		
		new Thread(){
			public void run(){
				restTemplate.postForObject(nextUrl
						+ "/receive/", player,
						Player.class);
			}
		}.start();

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	private void declareFinalResult(int idWinner) {
		List<Player> queue = playerService.findAll();
		for (Player player : queue) {
			if (player.getId() == idWinner) {
				player.setStatus(Status.WINNER);
			} else {
				player.setStatus(Status.LOSER);
			}
			restTemplate.postForObject(player.getUrl(), player, Player.class);
		}
	}

	private void renewRemoteStatus(Status status) {
		List<Player> queue = playerService.findAll();
		for (Player player : queue) {
			player.setStatus(status);
			player.setCurrentNumber(0);
			restTemplate.postForObject(player.getUrl(), player, Player.class);
		}
	}
	
	private void sincronizePlayers() {
		List<Player> queue = playerService.findAll();
		for (Player player : queue) {
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

}
