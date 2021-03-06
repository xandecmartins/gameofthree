package com.takeaway.gameofthree.player.strategies;

import org.springframework.beans.factory.annotation.Autowired;

import com.takeaway.gameofthree.player.AppProperties;

public class SmartStrategyImpl implements GameStrategy {

	@Autowired
	private AppProperties properties;

	@Override
	public int executeStrategy(int number) {
		int newNumber = number;

		int rest = number % properties.getDivisionReference();
		if (rest == 1) {
			newNumber--;
		} else if (rest == 2) {
			newNumber++;
		}

		newNumber /= properties.getDivisionReference();

		return newNumber;

	}
}
