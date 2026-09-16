package com.pkbob.roll4scape;

import net.runelite.client.ui.PluginPanel;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
public class Roll4ScapePanel extends PluginPanel
{
    public Roll4ScapePanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel(
                "<html><center><h2>Roll4Scape</h2>" +
                        "Can't decide what to do?<br>Roll for it.</center></html>",
                SwingConstants.CENTER
        );

        add(title, BorderLayout.NORTH);

        JPanel categoryPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        JButton bossingButton = new JButton("Bossing");
        JButton skillingButton = new JButton("Skilling");
        JButton makingButton = new JButton("Making");
        JButton combatButton = new JButton("Combat");
        JButton adventureButton = new JButton("Adventure");
        JButton wildButton = new JButton("Wild");
        JLabel rollResult = new JLabel("Choose a category to roll!", SwingConstants.CENTER);
        RollEngine rollEngine = new RollEngine();

        bossingButton.addActionListener(e ->
        {
            int roll = rollEngine.rollD20();
            BossingTask task = rollEngine.generateBossingTask(roll);

            rollResult.setText(
                    "<html><center>" +
                            "D20 Roll: " + roll + "<br>" +
                            "<b>" + task.getDisplayText() + "</b>" +
                            "</center></html>"
            );
        });
        categoryPanel.add(bossingButton);
        categoryPanel.add(skillingButton);
        categoryPanel.add(makingButton);
        categoryPanel.add(combatButton);
        categoryPanel.add(adventureButton);
        categoryPanel.add(wildButton);

        add(categoryPanel, BorderLayout.CENTER);
        add(rollResult, BorderLayout.SOUTH);
    }
}