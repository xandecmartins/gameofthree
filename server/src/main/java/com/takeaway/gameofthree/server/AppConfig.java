package com.takeaway.gameofthree.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.takeaway.gameofthree.server.service.PlayerService;
import com.takeaway.gameofthree.server.service.PlayerServiceImpl;

@Configuration
public class AppConfig {
	
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
	@Bean
	public PlayerService playerService(){
		return new PlayerServiceImpl();
	}

}