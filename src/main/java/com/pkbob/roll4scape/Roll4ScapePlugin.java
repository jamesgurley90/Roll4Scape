package com.pkbob.roll4scape;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
		name = "Roll4Scape"
)
public class Roll4ScapePlugin extends Plugin
{
	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private Client client;

	private NavigationButton navButton;
	private Roll4ScapePanel panel;

	@Override
	protected void startUp()
	{

		panel = new Roll4ScapePanel(client);

		navButton = NavigationButton.builder()
				.tooltip("Roll4Scape")
				.icon(ImageUtil.loadImageResource(getClass(), "roll4scape_icon.png"))
				.priority(5)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown()
	{

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