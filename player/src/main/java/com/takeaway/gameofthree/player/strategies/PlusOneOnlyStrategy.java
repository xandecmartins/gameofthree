package com.takeaway.gameofthree.player.strategies;

public class PlusOneOnlyStrategy implements GameStrategy{

	@Override
	public int executeStrategy(int number) {
		int newNumber = number;
		newNumber++;
		
		newNumber/=3;
		
		return newNumber;
		
	}
	
	
}
