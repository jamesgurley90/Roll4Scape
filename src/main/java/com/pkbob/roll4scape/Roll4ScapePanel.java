package com.pkbob.roll4scape;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.WorldType;
import net.runelite.client.ui.PluginPanel;

public class Roll4ScapePanel extends PluginPanel
{
    private final Client client;
    private final Roll4ScapeProgress progress;
    private final Runnable saveProgress;
    private final RollEngine rollEngine = new RollEngine();

    private int activeRoll = 0;
    private String activeCategory = null;
    private ActiveTask activeTask = null;


    /*
     * Exact NPC instance most recently engaged by the local player
     * while it matched the active Combat task.
     */
    private NPC trackedCombatNpc = null;

    private final JButton completeButton =
            new JButton("Complete Task");

    private final JLabel rollResult =
            new JLabel(
                    "Choose a category to roll!",
                    SwingConstants.CENTER
            );
    private final JLabel progressDisplay =
            new JLabel("", SwingConstants.CENTER);
    public Roll4ScapePanel(
            Client client,
            Roll4ScapeProgress progress,
            Runnable saveProgress)
    {
        this.client = client;
        this.progress = progress;
        this.saveProgress = saveProgress;

        setLayout(new BorderLayout());

        JLabel title = new JLabel(
                "<html><center><h2>Roll4Scape</h2>" +
                        "Can't decide what to do?<br>" +
                        "Roll for it.</center></html>",
                SwingConstants.CENTER
        );

        add(title, BorderLayout.NORTH);

        JPanel categoryPanel =
                new JPanel(
                        new GridLayout(
                                0,
                                2,
                                5,
                                5
                        )
                );

        JButton bossingButton =
                new JButton("Bossing");

        JButton skillingButton =
                new JButton("Skilling");

        JButton makingButton =
                new JButton("Making");

        JButton combatButton =
                new JButton("Combat");

        JButton adventureButton =
                new JButton("Adventure");

        JButton wildButton =
                new JButton("Wild");

        bossingButton.addActionListener(
                e -> rollBossing()
        );

        skillingButton.addActionListener(
                e -> rollGeneric(
                        rollEngine.getSkillingPool(),
                        false,
                        "SKILLING"
                )
        );

        makingButton.addActionListener(
                e -> rollGeneric(
                        rollEngine.getMakingPool(),
                        false,
                        "MAKING"
                )
        );

        combatButton.addActionListener(
                e -> rollCombat()
        );

        adventureButton.addActionListener(
                e -> rollGeneric(
                        rollEngine.getAdventurePool(),
                        false,
                        "ADVENTURE"
                )
        );

        wildButton.addActionListener(
                e -> rollGeneric(
                        rollEngine.getWildPool(),
                        true,
                        "WILD"
                )
        );

        completeButton.addActionListener(
                e -> completeActiveTask()
        );

        categoryPanel.add(bossingButton);
        categoryPanel.add(skillingButton);
        categoryPanel.add(makingButton);
        categoryPanel.add(combatButton);
        categoryPanel.add(adventureButton);
        categoryPanel.add(wildButton);
        categoryPanel.add(wildButton);
        categoryPanel.add(completeButton);

        JPanel centerPanel =
                new JPanel(new BorderLayout(0, 10));

        centerPanel.add(
                categoryPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                progressDisplay,
                BorderLayout.CENTER
        );

        add(centerPanel, BorderLayout.CENTER);
        add(rollResult, BorderLayout.SOUTH);

        refreshProgressDisplay();
    }

    private boolean canRoll()
    {
        if (client.getGameState() !=
                GameState.LOGGED_IN)
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
        return client.getWorldType()
                .contains(WorldType.MEMBERS);
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

        activeRoll = roll;
        activeCategory = "BOSSING";
        activeTask = null;
        trackedCombatNpc = null;

        BossingTask task =
                rollEngine.generateBossingTask(
                        roll,
                        isMembersWorld()
                );

        if (task == null)
        {

            clearActiveTask();
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
                        getCreditText(
                                roll,
                                false
                        ) +
                        "</center></html>"
        );
    }

    /*
     * -------------------------
     * COMBAT
     * -------------------------
     */
    private void rollCombat()
    {
        if (!canRoll())
        {
            return;
        }

        int roll = rollEngine.rollD20();

        CombatTask task =
                rollEngine.generateCombatTask(
                        client,
                        roll,
                        isMembersWorld()
                );

        if (task == null)
        {
            clearActiveTask();
            showNoTask();
            return;
        }

        activeRoll = roll;
        activeCategory = "COMBAT";
        trackedCombatNpc = null;

        activeTask = new ActiveTask(
                "COMBAT",
                task.getName(),
                task.getMinQuantity()
        );

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
                        "<br>Progress: <b>0 / " +
                        task.getMinQuantity() +
                        "</b>" +
                        requirementText +
                        getCreditText(
                                roll,
                                false
                        ) +
                        "</center></html>"
        );
    }

    /*
     * -------------------------
     * GENERIC CATEGORIES
     * -------------------------
     */
    private void rollGeneric(
            List<RollTask> pool,
            boolean wild,
            String category)
    {
        if (!canRoll())
        {
            return;
        }

        int roll = rollEngine.rollD20();

        RollTask task =
                rollEngine.generateTask(
                        pool,
                        roll,
                        isMembersWorld()
                );

        if (task == null)
        {
            clearActiveTask();
            showNoTask();
            return;
        }

        activeRoll = roll;
        activeCategory = category;
        activeTask = null;
        trackedCombatNpc = null;

        int quantity =
                task.getMinQuantity();

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
                        getCreditText(
                                roll,
                                wild
                        ) +
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

    private String getCreditText(
            int roll,
            boolean wild)
    {
        if (roll == 1)
        {
            if (wild)
            {
                return "<br>75% chance for " +
                        "<b>+2 task points</b>";
            }

            return "<br>50% chance for " +
                    "<b>+1 task point</b>";
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

    /*
     * -------------------------
     * TASK COMPLETION
     * -------------------------
     */
    private void completeActiveTask()
    {
        if (activeRoll == 0 ||
                activeCategory == null)
        {
            rollResult.setText(
                    "<html><center>" +
                            "<b>No active task to complete.</b>" +
                            "</center></html>"
            );

            return;
        }

        if ("COMBAT".equals(activeCategory))
        {
            if (activeTask == null)
            {
                rollResult.setText(
                        "<html><center>" +
                                "<b>No active Combat task found.</b>" +
                                "</center></html>"
                );

                return;
            }

            if (!activeTask.isComplete())
            {
                showCombatProgress(
                        "<b>Task not complete yet.</b><br>"
                );

                return;
            }
        }

        int completedRoll =
                activeRoll;

        String completedCategory =
                activeCategory;

        progress.completeTask(
                completedCategory
        );

        int rxpAwarded =
                progress.awardRxpForCompletedTask(
                        completedRoll
                );

        /*
         * Progress has changed, so save it immediately.
         */
        saveProgress.run();
        refreshProgressDisplay();

        clearActiveTask();

        rollResult.setText(
                "<html><center>" +
                        "<b>Task Complete!</b><br>" +
                        "Earned <b>" +
                        rxpAwarded +
                        " RXP</b><br>" +
                        "Roll Level: <b>" +
                        progress.getRollLevel() +
                        "</b><br>" +
                        "Current Streak: <b>" +
                        progress.getCurrentStreak() +
                        "</b>" +
                        "</center></html>"
        );
    }

    /*
     * Called when the local player starts interacting with an NPC.
     *
     * Only matching NPCs are remembered.
     */
    public void handlePlayerNpcInteraction(
            NPC npc)
    {
        if (npc == null ||
                activeTask == null ||
                !"COMBAT".equals(
                        activeTask.getCategory()))
        {
            return;
        }

        String npcName = npc.getName();

        if (npcName == null ||
                !npcName.equalsIgnoreCase(
                        activeTask.getTargetName()))
        {
            return;
        }

        trackedCombatNpc = npc;
    }

    /*
     * Count the death only when it is the exact NPC object
     * that the local player previously engaged.
     */
    public void handleActorDeath(
            Actor actor)
    {
        if (actor == null ||
                activeTask == null ||
                trackedCombatNpc == null ||
                !"COMBAT".equals(
                        activeTask.getCategory()))
        {
            return;
        }

        if (actor != trackedCombatNpc)
        {
            return;
        }

        String actorName =
                actor.getName();

        if (actorName == null ||
                !actorName.equalsIgnoreCase(
                        activeTask.getTargetName()))
        {
            trackedCombatNpc = null;
            return;
        }

        trackedCombatNpc = null;

        if (activeTask.isComplete())
        {
            return;
        }

        activeTask.addProgress();

        if (activeTask.isComplete())
        {
            showCombatProgress(
                    "<b>Combat task requirement complete!</b><br>" +
                            "Press <b>Complete Task</b> to claim it.<br>"
            );
        }
        else
        {
            showCombatProgress("");
        }
    }

    private void showCombatProgress(
            String message)
    {
        if (activeTask == null)
        {
            return;
        }

        rollResult.setText(
                "<html><center>" +
                        getRollHeading(activeRoll) +
                        "<br>" +
                        message +
                        "<b>Kill " +
                        activeTask.getRequiredAmount() +
                        " " +
                        activeTask.getTargetName() +
                        "</b><br>" +
                        "Progress: <b>" +
                        activeTask.getCurrentProgress() +
                        " / " +
                        activeTask.getRequiredAmount() +
                        "</b>" +
                        getCreditText(
                                activeRoll,
                                false
                        ) +
                        "</center></html>"
        );
    }
    public void refreshProgressDisplay()
    {
        int rxpNeeded =
                progress.getRxpNeededForNextLevel();

        String rxpText;

        if (progress.getRollLevel() >=
                Roll4ScapeProgress.MAX_ROLL_LEVEL)
        {
            rxpText = "MAX";
        }
        else
        {
            rxpText =
                    progress.getRxp() +
                            " / " +
                            rxpNeeded;
        }

        progressDisplay.setText(
                "<html><center>" +
                        "<hr>" +
                        "<b>ROLL4SCAPE PROGRESS</b><br><br>" +
                        "Roll Level: <b>" +
                        progress.getRollLevel() +
                        "</b><br>" +
                        "RXP: <b>" +
                        rxpText +
                        "</b><br>" +
                        "Current Streak: <b>" +
                        progress.getCurrentStreak() +
                        "</b><br>" +
                        "Best Streak: <b>" +
                        progress.getBestStreak() +
                        "</b><br>" +
                        "Tasks Completed: <b>" +
                        progress.getTotalTasksCompleted() +
                        "</b><br>" +
                        "Rerolls: <b>" +
                        progress.getRerolls() +
                        " / " +
                        Roll4ScapeProgress.MAX_REROLLS +
                        "</b>" +
                        "<hr>" +
                        "</center></html>"
        );
    }


    private void clearActiveTask()
    {
        activeRoll = 0;
        activeCategory = null;
        activeTask = null;
        trackedCombatNpc = null;
    }
}