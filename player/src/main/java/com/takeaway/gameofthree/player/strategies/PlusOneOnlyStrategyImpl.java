package com.takeaway.gameofthree.player.strategies;

import org.springframework.beans.factory.annotation.Autowired;

import com.takeaway.gameofthree.player.AppProperties;

public class PlusOneOnlyStrategyImpl implements GameStrategy{

	@Autowired
	private AppProperties properties;
	
	@Override
	public int executeStrategy(int number) {
		int newNumber = number;
		newNumber++;
		
		newNumber/=properties.getDivisionReference();
		
		return newNumber;
		
	}
	
	
}
