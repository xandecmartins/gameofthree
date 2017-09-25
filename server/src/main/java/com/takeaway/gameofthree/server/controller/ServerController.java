package com.takeaway.gameofthree.server.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.domain.Status;
import com.takeaway.gameofthree.server.service.PlayerService;

@RestController
@RequestMapping("/api")
public class ServerController {

	public static final Logger logger = LoggerFactory
			.getLogger(ServerController.class);

	private static final int LIMIT_NUMBER = 100;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PlayerService playerService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/player/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Player>> listAllPlayers() {
		List<Player> queue = playerService.findAll();
		if (queue.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Player>>(queue, HttpStatus.OK);
	}

	@RequestMapping(value = "/player/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable("id") int id) {
		logger.info("Fetching Player with id {}", id);
		return new ResponseEntity<Player>(playerService.findById(id),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/player/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePlayer(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting User with id {}", id);

		Player player = playerService.findById(id);
		playerService.remove(id);

		restTemplate.delete(player.getUrl() + "/disconnect");

		return new ResponseEntity<String>("the player was deleted",HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/start/player/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> startGamePlayer(@PathVariable("id") int id) {
		logger.info("Fetching & startingUser with id {}", id);

		if (playerService.isGameReady()) {
			renewRemoteStatus();

			Player player = playerService.startGame(id);

			restTemplate.getForObject(player.getUrl() + "/begin/{bound}",
					Integer.class, LIMIT_NUMBER);
		} else {
			logger.info("The amount of users is not enough to start");
			return new ResponseEntity<String>("The amount of users is not enough to start",HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<String>("The was started with sucess!",HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/register/{ip}/{port}", method = RequestMethod.GET)
	public String register(@PathVariable("ip") final String ip,
			@PathVariable("port") final int port) {
		logger.debug("Receiving new player..." + ip + " - " + port);
		return playerService.register(ip, port);
	}

	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public ResponseEntity<?> startGame() {
		logger.info("starting new game");
		if (playerService.isGameReady()) {

			renewRemoteStatus();

			Player player = playerService.startGame();
			restTemplate.getForObject(player.getUrl() + "/begin/{bound}",
					Integer.class, LIMIT_NUMBER);
		} else {
			logger.info("The amount of users is not enough to start");
			return new ResponseEntity<String>("The amount of users is not enough to start",HttpStatus.ACCEPTED);
		}
		
		return new ResponseEntity<Player>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/play/{number}/player/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> play(@PathVariable final int number, @PathVariable final int id) {

		if (!playerService.isGameReady()) {
			logger.info("There is not sufficient players available, wait for your opponent(s)!");
			return new ResponseEntity<String>("There is not sufficient players available, wait for your opponent(s)!",HttpStatus.ACCEPTED);
		}
		logger.info("Receiving new value " + number);

		Player player = playerService.findById(id);
		player.setCurrentNumber(number);

		// Simulate the time to view results
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (number == 1) {
			declareFinalResult(id);
			logger.info("Game is over!");
			return new ResponseEntity<String>("Game is over!",HttpStatus.ACCEPTED);
		}

		playerService.verifyRoundFinished();

		Player nextPlayer = playerService.retrieveNextPlayer();
		logger.info("Sending " + number + " to player " + nextPlayer.getId());
		restTemplate.getForObject(nextPlayer.getUrl() + "/receive/{number}",
				Integer.class, number);
		
		return new ResponseEntity<Player>(HttpStatus.NO_CONTENT);
	}

	private void declareFinalResult(int idWinner) {
		List<Player> queue = playerService.findAll();
		for (Player player : queue) {
			if (player.getId() == idWinner) {
				player.setStatus(Status.WINNER);
				restTemplate.getForObject(player.getUrl()
						+ "/newStatus/{status}", Integer.class, Status.WINNER);
			} else {
				player.setStatus(Status.LOSER);
				restTemplate.getForObject(player.getUrl()
						+ "/newStatus/{status}", Integer.class, Status.LOSER);
			}
		}
	}

	private void renewRemoteStatus() {
		List<Player> queue = playerService.findAll();
		for (Player player : queue) {
			player.setStatus(Status.READY);
			restTemplate.getForObject(player.getUrl() + "/newStatus/{status}",
					Integer.class, Status.WINNER);
		}
	}
	
}
