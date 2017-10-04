package com.takeaway.gameofthree.player.service;

import com.takeaway.gameofthree.domain.Player;
import com.takeaway.gameofthree.player.exception.IllegalMoveException;

public interface PlayerService {

	Player getPlayer();

	void update(Player player);
	
	void disconnect();
	
	void play(Player playerRemote);
	
	void manualPlay(int number) throws IllegalMoveException;
	
	void askToStart();
	
	void syncWithServer();
	
	void begin(int bound);
	
	void register(int port);

}
