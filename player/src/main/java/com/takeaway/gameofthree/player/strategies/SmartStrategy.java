package com.takeaway.gameofthree.player.strategies;

public class SmartStrategy implements GameStrategy{

	@Override
	public int executeStrategy(int number) {
		int newNumber = number;
		if(newNumber==4){
			newNumber--;
		} else if(newNumber==2){
			newNumber++;	
		} else if(newNumber!=3){
			newNumber++;
		}
		
		newNumber/=3;
		
		return newNumber;
		
	}
	
	
}
