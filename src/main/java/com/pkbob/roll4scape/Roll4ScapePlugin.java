package com.pkbob.roll4scape;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
@Slf4j
@PluginDescriptor(
	name = "Roll4Scape"
)
public class Roll4ScapePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private Roll4ScapeConfig config;
	@Inject
	private ClientToolbar clientToolbar;

	private NavigationButton navButton;
	private Roll4ScapePanel panel;
	@Override
	protected void startUp() throws Exception
	{
		log.debug("Roll4Scape started!");

		panel = new Roll4ScapePanel();

		navButton = NavigationButton.builder()
				.tooltip("Roll4Scape")
				.icon(ImageUtil.loadImageResource(getClass(), "roll4scape_icon.png"))
				.priority(5)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.debug("Roll4Scape stopped!");

		clientToolbar.removeNavigation(navButton);
		navButton = null;
		panel = null;
	}



	@Provides
	Roll4ScapeConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Roll4ScapeConfig.class);
	}
}
