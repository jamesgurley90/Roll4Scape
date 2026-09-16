package com.pkbob.roll4scape;

import net.runelite.client.ui.PluginPanel;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

public class Roll4ScapePanel extends PluginPanel
{
    public Roll4ScapePanel()
    {
        setLayout(new BorderLayout());

        JLabel title = new JLabel(
                "<html><center><h2>Roll4Scape</h2>" +
                        "Can't decide what to do?<br>Roll for it.</center></html>",
                SwingConstants.CENTER
        );

        add(title, BorderLayout.NORTH);
    }
}