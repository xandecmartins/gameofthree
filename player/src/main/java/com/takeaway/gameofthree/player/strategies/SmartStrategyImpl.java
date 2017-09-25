package com.takeaway.gameofthree.player.strategies;

import org.springframework.beans.factory.annotation.Autowired;

import com.takeaway.gameofthree.player.AppProperties;

public class SmartStrategyImpl implements GameStrategy{

	@Autowired
	private AppProperties properties;
	
	@Override
	public int executeStrategy(int number) {
		int newNumber = number;
		if(newNumber==properties.getDivisionReference()+1){
			newNumber--;
		} else if(newNumber==properties.getDivisionReference()-1){
			newNumber++;	
		} else if(newNumber!=properties.getDivisionReference()){
			newNumber++;
		}
		
		newNumber/=properties.getDivisionReference();
		
		return newNumber;
		
	}
	
	
}
