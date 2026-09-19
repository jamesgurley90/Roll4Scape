package com.pkbob.roll4scape;

import com.google.inject.Provides;
import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.InteractingChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
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

	@Inject
	private ConfigManager configManager;

	private Roll4ScapeProgress progress;

	private NavigationButton navButton;
	private Roll4ScapePanel panel;

	@Override
	protected void startUp()
	{
		progress = new Roll4ScapeProgress();

		panel = new Roll4ScapePanel(
				client,
				progress,
				this::saveProgress
		);

		navButton = NavigationButton.builder()
				.tooltip("Roll4Scape")
				.icon(ImageUtil.loadImageResource(
						getClass(),
						"roll4scape_icon.png"))
				.priority(5)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown()
	{
		/*
		 * Save one final time before the plugin shuts down.
		 */
		saveProgress();

		clientToolbar.removeNavigation(navButton);

		navButton = null;
		panel = null;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			loadProgress();
		}
	}

	@Subscribe
	public void onInteractingChanged(InteractingChanged event)
	{
		if (panel == null ||
				client.getLocalPlayer() == null ||
				event.getSource() != client.getLocalPlayer())
		{
			return;
		}

		if (event.getTarget() instanceof NPC)
		{
			panel.handlePlayerNpcInteraction(
					(NPC) event.getTarget()
			);
		}
	}

	@Subscribe
	public void onActorDeath(ActorDeath event)
	{
		if (panel == null)
		{
			return;
		}

		panel.handleActorDeath(event.getActor());
	}

	private String getPlayerSavePrefix()
	{
		if (client.getLocalPlayer() == null ||
				client.getLocalPlayer().getName() == null)
		{
			return null;
		}

		String playerName = client.getLocalPlayer()
				.getName()
				.toLowerCase()
				.replace(" ", "_");

		return "player_" + playerName + "_";
	}

	private void saveProgress()
	{
		String prefix = getPlayerSavePrefix();

		if (prefix == null || progress == null)
		{
			return;
		}

		configManager.setConfiguration(
				"roll4scape",
				prefix + "currentStreak",
				progress.getCurrentStreak());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "bestStreak",
				progress.getBestStreak());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "totalTasksCompleted",
				progress.getTotalTasksCompleted());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "rerolls",
				progress.getRerolls());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "rerollProgress",
				progress.getRerollProgress());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "rxp",
				progress.getRxp());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "rollLevel",
				progress.getRollLevel());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "prestige",
				progress.getPrestige());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "nat1Count",
				progress.getNat1Count());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "nat20Count",
				progress.getNat20Count());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "bossingTasksCompleted",
				progress.getBossingTasksCompleted());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "skillingTasksCompleted",
				progress.getSkillingTasksCompleted());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "makingTasksCompleted",
				progress.getMakingTasksCompleted());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "combatTasksCompleted",
				progress.getCombatTasksCompleted());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "adventureTasksCompleted",
				progress.getAdventureTasksCompleted());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "wildTasksCompleted",
				progress.getWildTasksCompleted());

		configManager.setConfiguration(
				"roll4scape",
				prefix + "equippedTitle",
				progress.getEquippedTitle());
	}

	private void loadProgress()
	{
		String prefix = getPlayerSavePrefix();

		if (prefix == null || progress == null)
		{
			return;
		}

		int currentStreak =
				getSavedInt(prefix + "currentStreak", 0);

		int bestStreak =
				getSavedInt(prefix + "bestStreak", 0);

		int totalTasksCompleted =
				getSavedInt(prefix + "totalTasksCompleted", 0);

		int rerolls =
				getSavedInt(prefix + "rerolls", 2);

		int rerollProgress =
				getSavedInt(prefix + "rerollProgress", 0);

		int rxp =
				getSavedInt(prefix + "rxp", 0);

		int rollLevel =
				getSavedInt(prefix + "rollLevel", 1);

		int prestige =
				getSavedInt(prefix + "prestige", 0);

		int nat1Count =
				getSavedInt(prefix + "nat1Count", 0);

		int nat20Count =
				getSavedInt(prefix + "nat20Count", 0);

		int bossingTasksCompleted =
				getSavedInt(
						prefix + "bossingTasksCompleted",
						0);

		int skillingTasksCompleted =
				getSavedInt(
						prefix + "skillingTasksCompleted",
						0);

		int makingTasksCompleted =
				getSavedInt(
						prefix + "makingTasksCompleted",
						0);

		int combatTasksCompleted =
				getSavedInt(
						prefix + "combatTasksCompleted",
						0);

		int adventureTasksCompleted =
				getSavedInt(
						prefix + "adventureTasksCompleted",
						0);

		int wildTasksCompleted =
				getSavedInt(
						prefix + "wildTasksCompleted",
						0);

		String equippedTitle =
				configManager.getConfiguration(
						"roll4scape",
						prefix + "equippedTitle"
				);

		progress.restoreProgress(
				currentStreak,
				bestStreak,
				totalTasksCompleted,
				rerolls,
				rerollProgress,
				rxp,
				rollLevel,
				prestige,
				nat1Count,
				nat20Count,
				bossingTasksCompleted,
				skillingTasksCompleted,
				makingTasksCompleted,
				combatTasksCompleted,
				adventureTasksCompleted,
				wildTasksCompleted,
				equippedTitle
		);
	}

	private int getSavedInt(
			String key,
			int defaultValue)
	{
		String value =
				configManager.getConfiguration(
						"roll4scape",
						key
				);

		if (value == null)
		{
			return defaultValue;
		}

		try
		{
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e)
		{
			return defaultValue;
		}
	}

	@Provides
	Roll4ScapeConfig provideConfig(
			ConfigManager configManager)
	{
		return configManager.getConfig(
				Roll4ScapeConfig.class
		);
	}
}