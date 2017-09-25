package com.takeaway.gameofthree.server.controller;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.takeaway.gameofthree.util.Generator;

@RestController
@RequestMapping("/api")
public class ServerController {

	public static final Logger logger = LoggerFactory
			.getLogger(ServerController.class);

	private static final int MIN_QUANTITY_PLAYERS = 2;
	private static final int MAX_QUANTITY_PLAYERS = 2;
	private static final int LIMIT_NUMBER = 100;

	@Autowired
	private RestTemplate restTemplate;
	
	private List<Player> queue;
	private Iterator<Player> iteratorPlayer;

	private boolean GAME_STARTED = false;

	public ServerController() {
		queue = new ArrayList<>();
	}

	@RequestMapping(value = "/player/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Player>> listAllPlayers() {
		if (queue.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Player>>(queue, HttpStatus.OK);
	}

	@RequestMapping(value = "/player/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable("id") int id) {
		logger.info("Fetching Player with id {}", id);
		Player player = getPlayer(id);
		if (player == null) {
			logger.error("User with id {} not found.", id);
			return new ResponseEntity("User with id " + id + " not found",
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Player>(player, HttpStatus.OK);
	}

	@RequestMapping(value = "/player/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePlayer(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting User with id {}", id);

		Player player = getPlayer(id);
		if (player == null) {
			logger.error("Unable to delete. User with id {} not found.", id);
			return new ResponseEntity("Unable to delete. User with id " + id
					+ " not found.", HttpStatus.NOT_FOUND);
		}

		removePlayer(id);
		try{
		restTemplate
				.delete(player.getUrl() + "/disconnect");
		} catch (Exception e){
			e.printStackTrace();
			logger.warn(e.getMessage());
		}

		return new ResponseEntity<Player>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/player/start/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> startGamePlayer(@PathVariable("id") int id) {
		logger.info("Fetching & startingUser with id {}", id);

		Player player = null;
		iteratorPlayer = queue.iterator();
		while (iteratorPlayer.hasNext()) {
			player = iteratorPlayer.next();
			if (player.getId() == id) {
				break;
			}
		}
		if (player == null) {
			logger.error("Unable to start, Player with id {} not found.", id);
			return new ResponseEntity("Unable to delete. User with id " + id
					+ " not found.", HttpStatus.NOT_FOUND);
		}

		restTemplate.getForObject(player.getUrl() + "/begin/{bound}",
				Integer.class, LIMIT_NUMBER);

		return new ResponseEntity<Player>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/register/{ip}/{port}", method = RequestMethod.GET)
	public String register(@PathVariable("ip") final String ip,
			@PathVariable("port") final int port) {
		logger.debug("Receiving new player...");
		String retVal;

		if (GAME_STARTED) {
			retVal = "The game has started, new players are not allowed!";
		} else if (isGameFull()) {
			retVal = "Number of players exceed";
		} else {
			Player player = new Player(Generator.getId(), ip, port);
			queue.add(player);
			retVal = "player " + player.getId();
			logger.info("Player " + player.getId() + " received...");
		}

		return retVal;
	}

	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public void startGame() {
		logger.info("starting new game");
		if (isGameReady()) {
			GAME_STARTED = Boolean.TRUE;
			iteratorPlayer = queue.iterator();
			Player player = iteratorPlayer.next();
			restTemplate.getForObject(player.getUrl() + "/begin/{bound}",
					Integer.class, LIMIT_NUMBER);
		} else {
			logger.info("The amount of users is not enough to start");
		}
	}

	@RequestMapping(value = "/play/{number}/player/{id}", method = RequestMethod.GET)
	public void play(@PathVariable final int number, @PathVariable final int id) {

		if (!isGameReady()) {
			logger.info("There is not sufficient players available, wait for your opponent(s)!");
			return;
		}
		logger.info("Receiving new value " + number);
		getPlayer(id).setCurrentNumber(number);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (number == 1) {
			declareFinalResult(id);

			logger.info("Game is over!");
			return;
		}

		if (!iteratorPlayer.hasNext()) {
			iteratorPlayer = queue.iterator();
		}

		Player player = iteratorPlayer.next();
		logger.info("Sending " + number + " to player " + player.getId());
		restTemplate.getForObject(player.getUrl() + "/receive/{number}",
				Integer.class, number);
	}

	private void removePlayer(int id) {
		Iterator<Player> itPlayer = queue.iterator();
		while (itPlayer.hasNext()) {
			Player player = itPlayer.next();
			if (player.getId() == id) {
				itPlayer.remove();
				break;
			}
		}
	}

	private Player getPlayer(int id) {
		Player retVal = null;
		for (Player playerQueue : queue) {
			if (playerQueue.getId() == id) {
				retVal = playerQueue;
				break;
			}
		}
		return retVal;
	}

	private void declareFinalResult(int idWinner) {
		for (Player player : queue) {
			if (player.getId() == idWinner) {
				player.setStatus(Status.WINNER);
				restTemplate.getForObject(player.getUrl() + "/finalResult/{status}",
						Integer.class, Status.WINNER);
			} else {
				player.setStatus(Status.LOSER);
				restTemplate.getForObject(player.getUrl() + "/finalResult/{status}",
						Integer.class, Status.LOSER);
			}
		}
	}

	private boolean isGameReady() {
		boolean retVal = false;
		if (queue.size() >= MIN_QUANTITY_PLAYERS) {
			retVal = true;
		}
		return retVal;
	}

	private boolean isGameFull() {
		boolean retVal = false;
		if (queue.size() == MAX_QUANTITY_PLAYERS) {
			retVal = true;
		}
		return retVal;
	}
}
