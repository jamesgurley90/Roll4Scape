package com.pkbob.roll4scape;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.WorldType;
import net.runelite.client.ui.PluginPanel;

public class Roll4ScapePanel extends PluginPanel
{
    private final Client client;
    private final RollEngine rollEngine = new RollEngine();

    private final JLabel rollResult =
            new JLabel("Choose a category to roll!", SwingConstants.CENTER);

    public Roll4ScapePanel(Client client)
    {
        this.client = client;

        setLayout(new BorderLayout());

        JLabel title = new JLabel(
                "<html><center><h2>Roll4Scape</h2>" +
                        "Can't decide what to do?<br>" +
                        "Roll for it.</center></html>",
                SwingConstants.CENTER
        );

        add(title, BorderLayout.NORTH);

        JPanel categoryPanel =
                new JPanel(new GridLayout(0, 2, 5, 5));

        JButton bossingButton = new JButton("Bossing");
        JButton skillingButton = new JButton("Skilling");
        JButton makingButton = new JButton("Making");
        JButton combatButton = new JButton("Combat");
        JButton adventureButton = new JButton("Adventure");
        JButton wildButton = new JButton("Wild");

        bossingButton.addActionListener(e -> rollBossing());

        skillingButton.addActionListener(e -> rollGeneric(
                rollEngine.getSkillingPool(),
                false
        ));

        makingButton.addActionListener(e -> rollGeneric(
                rollEngine.getMakingPool(),
                false
        ));

        combatButton.addActionListener(e -> rollCombat());

        adventureButton.addActionListener(e -> rollGeneric(
                rollEngine.getAdventurePool(),
                false
        ));

        wildButton.addActionListener(e -> rollGeneric(
                rollEngine.getWildPool(),
                true
        ));

        categoryPanel.add(bossingButton);
        categoryPanel.add(skillingButton);
        categoryPanel.add(makingButton);
        categoryPanel.add(combatButton);
        categoryPanel.add(adventureButton);
        categoryPanel.add(wildButton);

        add(categoryPanel, BorderLayout.CENTER);
        add(rollResult, BorderLayout.SOUTH);
    }

    /*
     * Prevent rolling before the player is actually logged in.
     *
     * Before login RuneLite does not yet give us reliable account/world
     * information for Roll4Scape's filtering.
     */
    private boolean canRoll()
    {
        if (client.getGameState() != GameState.LOGGED_IN)
        {
            rollResult.setText(
                    "<html><center>" +
                            "<b>Log in to OSRS before rolling.</b>" +
                            "</center></html>"
            );

            return false;
        }

        return true;
    }

    private boolean isMembersWorld()
    {
        return client.getWorldType().contains(WorldType.MEMBERS);
    }

    /*
     * -------------------------
     * BOSSING
     * -------------------------
     */
    private void rollBossing()
    {
        if (!canRoll())
        {
            return;
        }

        int roll = rollEngine.rollD20();

        BossingTask task = rollEngine.generateBossingTask(
                roll,
                isMembersWorld()
        );

        if (task == null)
        {
            showNoTask();
            return;
        }

        rollResult.setText(
                "<html><center>" +
                        getRollHeading(roll) +
                        "<br><b>" +
                        task.getAction() + " " +
                        task.getMinQuantity() + " " +
                        task.getBossName() +
                        "</b>" +
                        getCreditText(roll, false) +
                        "</center></html>"
        );
    }

    /*
     * -------------------------
     * COMBAT
     * -------------------------
     *
     * Combat is now account-aware.
     *
     * The RollEngine receives the RuneLite Client so it can read the
     * player's actual combat and Slayer levels.
     */
    private void rollCombat()
    {
        if (!canRoll())
        {
            return;
        }

        int roll = rollEngine.rollD20();

        CombatTask task = rollEngine.generateCombatTask(
                client,
                roll,
                isMembersWorld()
        );

        if (task == null)
        {
            showNoTask();
            return;
        }

        String requirementText = "";

        if (!task.getRequirementNote().isEmpty())
        {
            requirementText =
                    "<br><small>" +
                            task.getRequirementNote() +
                            "</small>";
        }

        rollResult.setText(
                "<html><center>" +
                        getRollHeading(roll) +
                        "<br><b>Kill " +
                        task.getMinQuantity() + " " +
                        task.getName() +
                        "</b>" +
                        requirementText +
                        getCreditText(roll, false) +
                        "</center></html>"
        );
    }

    /*
     * -------------------------
     * GENERIC CATEGORIES
     * -------------------------
     *
     * Skilling, Making, Adventure and the current Wild prototype
     * still use RollTask.
     */
    private void rollGeneric(
            List<RollTask> pool,
            boolean wild)
    {
        if (!canRoll())
        {
            return;
        }

        int roll = rollEngine.rollD20();

        RollTask task = rollEngine.generateTask(
                pool,
                roll,
                isMembersWorld()
        );

        if (task == null)
        {
            showNoTask();
            return;
        }

        int quantity = task.getMinQuantity();

        /*
         * NAT 1 tasks are deliberately tiny.
         *
         * Eventually Nat 1 will use its own curated fumble pool.
         */
        if (roll == 1)
        {
            quantity = 1;
        }

        rollResult.setText(
                "<html><center>" +
                        getRollHeading(roll) +
                        "<br><b>" +
                        task.getAction() + " " +
                        quantity + " " +
                        task.getName() +
                        "</b>" +
                        getCreditText(roll, wild) +
                        "</center></html>"
        );
    }

    /*
     * -------------------------
     * D20 DISPLAY
     * -------------------------
     */
    private String getRollHeading(int roll)
    {
        if (roll == 1)
        {
            return "<b>NAT 1!</b>";
        }

        if (roll == 20)
        {
            return "<b>NAT 20!</b>";
        }

        return "D20 Roll: " + roll;
    }

    /*
     * This displays what completing the task will eventually be worth.
     *
     * We are NOT actually saving streak/task-point progress yet.
     * That comes with the progression system.
     */
    private String getCreditText(
            int roll,
            boolean wild)
    {
        if (roll == 1)
        {
            if (wild)
            {
                return "<br>75% chance for <b>+2 task points</b>";
            }

            return "<br>50% chance for <b>+1 task point</b>";
        }

        if (roll == 20)
        {
            return wild
                    ? "<br>Worth <b>+4 task points</b>"
                    : "<br>Worth <b>+2 task points</b>";
        }

        return wild
                ? "<br>Worth <b>+2 task points</b>"
                : "<br>Worth <b>+1 task point</b>";
    }

    private void showNoTask()
    {
        rollResult.setText(
                "<html><center>" +
                        "<b>No eligible task found for this account.</b>" +
                        "</center></html>"
        );
    }
}