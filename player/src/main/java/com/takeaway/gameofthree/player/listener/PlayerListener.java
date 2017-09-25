package com.takeaway.gameofthree.player.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.takeaway.gameofthree.player.controller.PlayerController;

@Component
public class PlayerListener implements
		ApplicationListener<EmbeddedServletContainerInitializedEvent> {

	@Autowired
	private PlayerController playerController;
	
	@Override
	public void onApplicationEvent(
			final EmbeddedServletContainerInitializedEvent event) {
		int port = event.getEmbeddedServletContainer().getPort();
		playerController.registerPlayer(port);
	}
}