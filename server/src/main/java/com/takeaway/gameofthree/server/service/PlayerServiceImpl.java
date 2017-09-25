package com.takeaway.gameofthree.server.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.util.Generator;

public class PlayerServiceImpl implements PlayerService {

	private static final int MIN_QUANTITY_PLAYERS = 2;
	
	private static final int MAX_QUANTITY_PLAYERS = 2;
	
	private boolean gameStarted;
	
	private List<Player> queue;
	private Iterator<Player> iteratorGame;

	public PlayerServiceImpl() {
		queue = new ArrayList<>();
	}

	public List<Player> findAll() {
		return queue;
	}

	public void add(Player player) {
		queue.add(player);
	}

	public void remove(int id) {
		Iterator<Player> itPlayer = queue.iterator();
		while (itPlayer.hasNext()) {
			Player player = itPlayer.next();
			if (player.getId() == id) {
				itPlayer.remove();
				break;
			}
		}
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

	public Player adjustPositionGame(int id) {
		Player player = null;
		iteratorGame = queue.iterator();
		while (iteratorGame.hasNext()) {
			player = iteratorGame.next();
			if (player.getId() == id) {
				break;
			}
		}
		return player;
	}

	public Player renewRound() {
		iteratorGame = queue.iterator();
		return iteratorGame.next();
	}

	public void verifyRoundFinished() {
		if (!iteratorGame.hasNext()) {
			iteratorGame = queue.iterator();
		}
	}

	public Player retrieveNextPlayer() {
		return iteratorGame.next();
	}
	
	public boolean isGameReady() {
		boolean retVal = false;
		if (queue.size() >= MIN_QUANTITY_PLAYERS) {
			retVal = true;
		}
		return retVal;
	}

	public boolean isGameFull() {
		boolean retVal = false;
		if (queue.size() == MAX_QUANTITY_PLAYERS) {
			retVal = true;
		}
		return retVal;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}
	
	public String register(String ip, int port){
		String retVal;
		if (isGameStarted()) {
			retVal = "The game has started, new players are not allowed!";
		} else if (isGameFull()) {
			retVal = "Number of players exceed";
		} else {
			Player player = new Player(Generator.getId(), ip, port);
			add(player);
			retVal = "player " + player.getId();
		}
		return retVal;
	}

	public Player startGame(int id) {
		setGameStarted(Boolean.TRUE);
		adjustPositionGame(id);
		return renewRound();
		
	}
	
	public Player startGame() {
		setGameStarted(Boolean.TRUE);
		return renewRound();
		
	}
	
	

}
