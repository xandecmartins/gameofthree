package com.takeaway.gameofthree.server.service;

import java.util.List;

import com.takeaway.gameofthree.domain.Player;

public interface PlayerService {

	void add(Player player);

	void remove(int id);

	List<Player> findAll();

	Player findById(int id);

	Player adjustPositionGame(int id);

	Player renewRound();

	void verifyRoundFinished();

	Player retrieveNextPlayer();

	boolean isGameReady();

	boolean isGameFull();

	boolean isGameStarted();

	void setGameStarted(boolean gameStarted);

	String register(String ip, int port);
	
	Player startGame(int id);
	
	Player startGame();
	
}
