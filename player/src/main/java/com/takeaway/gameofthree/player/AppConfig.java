package com.takeaway.gameofthree.player;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.takeaway.gameofthree.player.service.PlayerService;
import com.takeaway.gameofthree.player.service.PlayerServiceImpl;
import com.takeaway.gameofthree.player.strategies.GameStrategy;
import com.takeaway.gameofthree.player.strategies.SmartStrategyImpl;

@Configuration
public class AppConfig {

	@Bean
	public GameStrategy gameStrategy(){
		return new SmartStrategyImpl();
	}
	
	@Bean
	public PlayerService playerService(){
		return new PlayerServiceImpl();
	}
	
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}