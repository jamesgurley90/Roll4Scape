package com.pkbob.roll4scape;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class Roll4ScapePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(Roll4ScapePlugin.class);
		RuneLite.main(args);
	}
}