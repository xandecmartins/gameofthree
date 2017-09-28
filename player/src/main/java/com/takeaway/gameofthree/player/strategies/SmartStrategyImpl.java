package com.takeaway.gameofthree.player.strategies;

import org.springframework.beans.factory.annotation.Autowired;

import com.takeaway.gameofthree.player.AppProperties;

public class SmartStrategyImpl implements GameStrategy{

	@Autowired
	private AppProperties properties;
	
	@Override
	public int executeStrategy(int number) {
		int newNumber = number;
		
		int rest = number % 3;
		if(rest==1){
			newNumber--;
		} else if(newNumber==2){
			newNumber++;	
		} 
		
		newNumber/=properties.getDivisionReference();
		
		return newNumber;
		
	}
	
	public static void main(String[] args) {
		System.out.println(new SmartStrategyImpl().executeStrategy(16));
	}
	
	
}
