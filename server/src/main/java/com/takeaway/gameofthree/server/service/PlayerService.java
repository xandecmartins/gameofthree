package com.takeaway.gameofthree.server.service;

import java.util.List;

import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.server.exception.GameException;

public interface PlayerService {

	Player add(Player player) throws GameException;

	Player update(Player player);

	void remove(Player player);

	List<Player> findAll();

	Player findById(int id);

	void startGame(int id) throws GameException;

	void play(Player player) throws GameException;

}
