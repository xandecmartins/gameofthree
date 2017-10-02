package com.takeaway.gameofthree.server.service;

import java.util.List;

import com.takeaway.gameofthree.domain.Player;

public interface PlayerService {

	Player add(Player player);
	
	Player update(Player player);

	Player remove(int id);

	List<Player> findAll();

	Player findById(int id);

	Player setGamePosition(int idPlayer);

	Player renewRound();

	boolean verifyRoundIsFinished();

	Player getNextPlayer();

	boolean isGameReadyToStart();

	boolean isGameFull();

	boolean isGameStarted();

	void setGameStarted(boolean gameStarted);

	Player startGame(int id);
	
	Player startGame();
	
}
