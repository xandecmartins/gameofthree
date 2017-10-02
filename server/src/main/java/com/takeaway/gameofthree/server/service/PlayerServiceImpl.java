package com.takeaway.gameofthree.server.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.domain.Status;
import com.takeaway.gameofthree.server.AppProperties;
import com.takeaway.gameofthree.util.Generator;

public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private AppProperties properties;

	private boolean gameStarted;

	private List<Player> queue;
	private Iterator<Player> iteratorGame;

	public PlayerServiceImpl() {
		queue = new ArrayList<>();
	}

	public List<Player> findAll() {
		return queue;
	}

	public Player add(Player player) {
		if(player.getId() == null){
			player.setId(Generator.getId());
			queue.add(player);
		}
		
		if(isGameReadyToStart()){
			for (Player storedPlayer : queue) {
				storedPlayer.setStatus(Status.READY);
			}
		}
		
		return player;
	}

	public Player remove(int id) {
		Iterator<Player> itPlayer = queue.iterator();
		Player player = null;
		while (itPlayer.hasNext()) {
			player = itPlayer.next();
			if (player.getId() == id) {
				itPlayer.remove();
				break;
			}
		}
		
		if(!isGameReadyToStart()){
			for (Player storedPlayer : queue) {
				storedPlayer.setStatus(Status.WAITING);
			}
		}
		
		return player;
	}
	
	public Player update(Player player) {
		for (Player tempPlayer : queue) {
			if(tempPlayer.equals(player)){
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

	public Player setGamePosition(int idPlayer) {
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

	public Player renewRound() {
		iteratorGame = queue.iterator();
		return iteratorGame.next();
	}

	public boolean verifyRoundIsFinished() {
		return !iteratorGame.hasNext();
	}
	
	public Player getNextPlayer() {
		return iteratorGame.next();
	}

	public boolean isGameReadyToStart() {
		boolean retVal = false;
		if (queue.size() >= properties.getMinUsers()) {
			retVal = true;
		}
		return retVal;
	}

	public boolean isGameFull() {
		boolean retVal = false;
		if (queue.size() == properties.getMaxUsers()) {
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

	public Player startGame(int idPlayer) {
		setGameStarted(Boolean.TRUE);
		setGamePosition(idPlayer);
		return renewRound();

	}

	public Player startGame() {
		setGameStarted(Boolean.TRUE);
		return renewRound();

	}

}
