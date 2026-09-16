package com.pkbob.roll4scape;

import net.runelite.client.ui.PluginPanel;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import net.runelite.api.Client;
import net.runelite.api.WorldType;
public class Roll4ScapePanel extends PluginPanel
{
    private final Client client;
    public Roll4ScapePanel(Client client)
    {
        this.client = client;
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
        boolean isMembers = client.getWorldType().contains(WorldType.MEMBERS);

        int roll = rollEngine.rollD20();
        BossingTask task = rollEngine.generateBossingTask(roll, isMembers);

            rollResult.setText(
                    "<html><center>" +
                            "D20 Roll: " + roll + "<br>" +
                            "<b>" + task.getAction() + " " + task.getMinQuantity() + " " + task.getBossName() + "</b>" +
                            "</center></html>"
            );
        });
        skillingButton.addActionListener(e ->
        {
            boolean isMembers = client.getWorldType().contains(WorldType.MEMBERS);

            int roll = rollEngine.rollD20();
            RollTask task = rollEngine.generateTask(rollEngine.getSkillingPool(), roll, isMembers);

            rollResult.setText(
                    "<html><center>" +
                            "D20 Roll: " + roll + "<br>" +
                            "<b>" + task.getAction() + " " + task.getMinQuantity() + " " + task.getName() + "</b>" +
                            "</center></html>"
            );
        });
        makingButton.addActionListener(e ->
        {
            boolean isMembers = client.getWorldType().contains(WorldType.MEMBERS);

            int roll = rollEngine.rollD20();
            RollTask task = rollEngine.generateTask(rollEngine.getMakingPool(), roll, isMembers);

            rollResult.setText(
                    "<html><center>" +
                            "D20 Roll: " + roll + "<br>" +
                            "<b>" + task.getAction() + " " + task.getMinQuantity() + " " + task.getName() + "</b>" +
                            "</center></html>"
            );
        });
        makingButton.addActionListener(e ->
        {
            boolean isMembers = client.getWorldType().contains(WorldType.MEMBERS);

            int roll = rollEngine.rollD20();
            RollTask task = rollEngine.generateTask(rollEngine.getMakingPool(), roll, isMembers);

            rollResult.setText(
                    "<html><center>" +
                            "D20 Roll: " + roll + "<br>" +
                            "<b>" + task.getAction() + " " + task.getMinQuantity() + " " + task.getName() + "</b>" +
                            "</center></html>"
            );
        });
        combatButton.addActionListener(e ->
        {
            boolean isMembers = client.getWorldType().contains(WorldType.MEMBERS);

            int roll = rollEngine.rollD20();
            RollTask task = rollEngine.generateTask(rollEngine.getCombatPool(), roll, isMembers);

            rollResult.setText(
                    "<html><center>" +
                            "D20 Roll: " + roll + "<br>" +
                            "<b>" + task.getAction() + " " + task.getMinQuantity() + " " + task.getName() + "</b>" +
                            "</center></html>"
            );
        });
        adventureButton.addActionListener(e ->
        {
            boolean isMembers = client.getWorldType().contains(WorldType.MEMBERS);

            int roll = rollEngine.rollD20();
            RollTask task = rollEngine.generateTask(rollEngine.getAdventurePool(), roll, isMembers);

            rollResult.setText(
                    "<html><center>" +
                            "D20 Roll: " + roll + "<br>" +
                            "<b>" + task.getAction() + " " + task.getMinQuantity() + " " + task.getName() + "</b>" +
                            "</center></html>"
            );
        });
        wildButton.addActionListener(e ->
        {
            boolean isMembers = client.getWorldType().contains(WorldType.MEMBERS);

            int roll = rollEngine.rollD20();
            RollTask task = rollEngine.generateTask(rollEngine.getWildPool(), roll, isMembers);

            rollResult.setText(
                    "<html><center>" +
                            "D20 Roll: " + roll + "<br>" +
                            "<b>" + task.getAction() + " " + task.getMinQuantity() + " " + task.getName() + "</b>" +
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