package com.takeaway.gameofthree.player.controller;

import org.json.JSONObject;
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
import org.springframework.web.client.HttpClientErrorException;

import com.takeaway.gameofthree.domain.CustomErrorType;
import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.player.service.PlayerService;

@RestController
@RequestMapping("/api")
public class PlayerController {

	public static final Logger logger = LoggerFactory
			.getLogger(PlayerController.class);

	@Autowired
	private PlayerService playerService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Player getPlayer() {
		return playerService.getPlayer();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<?> updtaePlayer(@RequestBody Player player) {
		try {
			playerService.update(player);
		} catch (Exception e) {
			return handleException(e);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/receive", method = RequestMethod.POST)
	public ResponseEntity<?> receive(@RequestBody Player playerRemote) {
		logger.info("Receiving number " + playerRemote.getCurrentNumber());
		try {
			playerService.play(playerRemote);
		} catch (Exception e) {
			return handleException(e);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{number}/manual_play", method = RequestMethod.POST)
	public ResponseEntity<?> manualPlay(@PathVariable final int number) {
		try {
			playerService.manualPlay(number);
		} catch (Exception e) {
			return handleException(e);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public ResponseEntity<?> startGame() {
		try {
			playerService.askToStart();
		} catch (Exception e) {
			return handleException(e);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{autonomous}/change", method = RequestMethod.POST)
	public ResponseEntity<?> updatePlayer(@PathVariable final boolean autonomous) {
		logger.info("switching user to autonomous " + autonomous);
		try {
			playerService.getPlayer().setAutonomous(autonomous);
			playerService.syncWithServer();
		} catch (Exception e) {
			return handleException(e);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/mark_new_value", method = RequestMethod.POST)
	public ResponseEntity<?> startNewValue() {
		logger.info("receiving new value");
		try {
			playerService.getPlayer().setHaveNewValue(false);
		} catch (Exception e) {
			return handleException(e);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/game/{bound}/begin", method = RequestMethod.POST)
	public ResponseEntity<?> begin(@PathVariable final int bound) {
		try {
			playerService.begin(bound);
		} catch (Exception e) {
			return handleException(e);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public ResponseEntity<?> disconect() {
		try {
			playerService.disconnect();
		} catch (Exception e) {
			return handleException(e);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	private ResponseEntity<?> handleException(Exception e) {
		if (e instanceof HttpClientErrorException) {
			return new ResponseEntity<CustomErrorType>(new CustomErrorType(
					new JSONObject(
							((HttpClientErrorException) e)
									.getResponseBodyAsString())),
					HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<CustomErrorType>(new CustomErrorType(
					e.getMessage()), HttpStatus.BAD_REQUEST);
		}

	}
}
