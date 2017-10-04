package com.takeaway.gameofthree.player.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.takeaway.gameofthree.player.service.PlayerService;

@Component
public class PlayerListener implements
		ApplicationListener<EmbeddedServletContainerInitializedEvent> {

	@Autowired
	private PlayerService playerService;
	
	@Override
	public void onApplicationEvent(
			final EmbeddedServletContainerInitializedEvent event) {
		playerService.register(event.getEmbeddedServletContainer().getPort());
	}
}