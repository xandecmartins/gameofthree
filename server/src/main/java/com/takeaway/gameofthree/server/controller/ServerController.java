package com.takeaway.gameofthree.server.controller;

import java.util.List;

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

import com.takeaway.gameofthree.domain.CustomErrorType;
import com.takeaway.gameofthree.domain.CustomSuccessType;
import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.server.exception.GameException;
import com.takeaway.gameofthree.server.exception.GameOverException;
import com.takeaway.gameofthree.server.service.PlayerService;

@RestController
@RequestMapping("/api")
public class ServerController {

	public static final Logger logger = LoggerFactory
			.getLogger(ServerController.class);

	@Autowired
	private PlayerService playerService;

	@RequestMapping(value = "/players/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllPlayers() {
		List<Player> queue = playerService.findAll();
		if (queue.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Player>>(queue, HttpStatus.OK);
	}

	@RequestMapping(value = "/players/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPlayer(@PathVariable("id") int id) {
		logger.info("Fetching Player with id {}", id);
		return new ResponseEntity<Player>(playerService.findById(id),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/players/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePlayer(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting User with id {}", id);
		playerService.remove(new Player(id));
		return new ResponseEntity<CustomSuccessType>(new CustomSuccessType(
				"Player has been deleted!"), HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/players/{id}/start", method = RequestMethod.POST)
	public ResponseEntity<?> startGameWithPlayer(@PathVariable("id") int id) {
		logger.info("Fetching & startingUser with id {}", id);
		try {
			playerService.startGame(id);

		} catch (GameException e) {
			logger.info(e.getMessage());
			return new ResponseEntity<CustomErrorType>(new CustomErrorType(
					e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<CustomSuccessType>(new CustomSuccessType(
				"Game started!"), HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/players/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePlayer(@RequestBody Player player,
			@PathVariable("id") int id) {
		logger.debug("Updating player..." + player.getId() + " - "
				+ player.getAutonomous());
		playerService.update(player);
		return new ResponseEntity<CustomSuccessType>(new CustomSuccessType(
				"Player has been updated!"), HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/players", method = RequestMethod.POST)
	public ResponseEntity<?> addPlayer(@RequestBody Player player) {
		logger.debug("Receiving new player..." + player.getIp() + " - "
				+ player.getPort());
		try {
			playerService.add(player);
		} catch (GameException e) {
			logger.info(e.getMessage());
			return new ResponseEntity<CustomErrorType>(new CustomErrorType(
					e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Player>(player, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/players/{id}/play", method = RequestMethod.POST)
	public ResponseEntity<?> play(@PathVariable final int id,
			@RequestBody Player player) {
		try {
			playerService.play(player);
		} catch (GameOverException e) {
			logger.info(e.getMessage());
			return new ResponseEntity<CustomSuccessType>(new CustomSuccessType(
					e.getMessage()), HttpStatus.ACCEPTED);
		} catch (GameException e) {
			logger.info(e.getMessage());
			return new ResponseEntity<CustomErrorType>(new CustomErrorType(
					e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(HttpStatus.ACCEPTED);
	}

	
	
	

}
