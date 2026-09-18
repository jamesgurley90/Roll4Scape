package com.pkbob.roll4scape;

public class Roll4ScapeProgress
{
    public static final int MAX_REROLLS = 3;
    public static final int TASKS_PER_REROLL = 5;
    public static final int MAX_ROLL_LEVEL = 99;

    private int currentStreak;
    private int bestStreak;
    private int totalTasksCompleted;

    private int rerolls = 2;
    private int rerollProgress;

    private int rxp;
    private int rollLevel = 1;
    private int prestige;

    private int nat1Count;
    private int nat20Count;

    private int bossingTasksCompleted;
    private int skillingTasksCompleted;
    private int makingTasksCompleted;
    private int combatTasksCompleted;
    private int adventureTasksCompleted;
    private int wildTasksCompleted;

    private String equippedTitle = "";

    public int getCurrentStreak()
    {
        return currentStreak;
    }

    public int getBestStreak()
    {
        return bestStreak;
    }

    public int getTotalTasksCompleted()
    {
        return totalTasksCompleted;
    }

    public int getRerolls()
    {
        return rerolls;
    }

    public int getRerollProgress()
    {
        return rerollProgress;
    }

    public int getRxp()
    {
        return rxp;
    }

    public int getRollLevel()
    {
        return rollLevel;
    }

    public int getPrestige()
    {
        return prestige;
    }

    public int getNat1Count()
    {
        return nat1Count;
    }

    public int getNat20Count()
    {
        return nat20Count;
    }

    public int getBossingTasksCompleted()
    {
        return bossingTasksCompleted;
    }

    public int getSkillingTasksCompleted()
    {
        return skillingTasksCompleted;
    }

    public int getMakingTasksCompleted()
    {
        return makingTasksCompleted;
    }

    public int getCombatTasksCompleted()
    {
        return combatTasksCompleted;
    }

    public int getAdventureTasksCompleted()
    {
        return adventureTasksCompleted;
    }

    public int getWildTasksCompleted()
    {
        return wildTasksCompleted;
    }

    public String getEquippedTitle()
    {
        return equippedTitle;
    }
    public void completeTask(String category)
    {
        totalTasksCompleted++;

        currentStreak++;
        if (currentStreak > bestStreak)
        {
            bestStreak = currentStreak;
        }



        if (rerolls < MAX_REROLLS)
        {
            rerollProgress++;

            if (rerollProgress >= TASKS_PER_REROLL)
            {
                rerolls++;
                rerollProgress = 0;
            }
        }
        else
        {
            rerollProgress = TASKS_PER_REROLL - 1;
        }

        switch (category.toUpperCase())
        {
            case "BOSSING":
                bossingTasksCompleted++;
                break;
            case "SKILLING":
                skillingTasksCompleted++;
                break;
            case "MAKING":
                makingTasksCompleted++;
                break;
            case "COMBAT":
                combatTasksCompleted++;
                break;
            case "ADVENTURE":
                adventureTasksCompleted++;
                break;
            case "WILD":
                wildTasksCompleted++;
                break;
            default:
                break;
        }
    }

    public boolean useReroll()
    {
        if (rerolls <= 0)
        {
            return false;
        }

        rerolls--;
        return true;
    }

    public void quitTask()
    {
        currentStreak = 0;
    }

    public void recordRoll(int roll)
    {
        if (roll == 1)
        {
            nat1Count++;
        }
        else if (roll == 20)
        {
            nat20Count++;
        }
    }
    public int getTasksNeededForNextLevel()
    {
        if (rollLevel >= MAX_ROLL_LEVEL)
        {
            return 0;
        }

        // Gradually increases from about 2 tasks per level
        // at low levels to about 13 tasks near level 99.
        return 2 + (int) Math.floor((rollLevel - 1) * 11.0 / 97.0);
    }

    public int getBaseRxpForLevel()
    {
        // RXP numbers grow as the player's Roll4Scape level grows.
        return 100 + ((rollLevel - 1) * 20);
    }

    public int getRxpForRoll(int roll)
    {
        int baseRxp = getBaseRxpForLevel();

        if (roll == 1)
        {
            // Nat 1 is handled separately because it only has
            // a 25% chance of actually awarding RXP.
            return Math.max(25, baseRxp / 4);
        }

        if (roll == 20)
        {
            return baseRxp * 2;
        }

        return (int) Math.round(baseRxp * (roll / 10.0));
    }

    public int getRxpNeededForNextLevel()
    {
        if (rollLevel >= MAX_ROLL_LEVEL)
        {
            return 0;
        }

        /*
         * An average normal D20 roll is worth approximately
         * the level's base RXP. Multiply that by our target
         * number of tasks for this level.
         */
        return getBaseRxpForLevel() * getTasksNeededForNextLevel();
    }
    public int awardRxpForCompletedTask(int roll)
    {
        if (rollLevel >= MAX_ROLL_LEVEL)
        {
            return 0;
        }

        // Nat 1 only has a 25% chance to award RXP.
        if (roll == 1 && Math.random() >= 0.25)
        {
            return 0;
        }

        int awardedRxp = getRxpForRoll(roll);
        rxp += awardedRxp;

        checkForLevelUp();

        return awardedRxp;
    }

    private void checkForLevelUp()
    {
        while (rollLevel < MAX_ROLL_LEVEL)
        {
            int needed = getRxpNeededForNextLevel();

            if (rxp < needed)
            {
                break;
            }

            rxp -= needed;
            rollLevel++;
        }

        if (rollLevel >= MAX_ROLL_LEVEL)
        {
            rollLevel = MAX_ROLL_LEVEL;
            rxp = 0;
        }
    }
    public void restoreProgress(
            int currentStreak,
            int bestStreak,
            int totalTasksCompleted,
            int rerolls,
            int rerollProgress,
            int rxp,
            int rollLevel,
            int prestige,
            int nat1Count,
            int nat20Count,
            int bossingTasksCompleted,
            int skillingTasksCompleted,
            int makingTasksCompleted,
            int combatTasksCompleted,
            int adventureTasksCompleted,
            int wildTasksCompleted,
            String equippedTitle)
    {
        this.currentStreak = Math.max(0, currentStreak);
        this.bestStreak = Math.max(this.currentStreak, bestStreak);
        this.totalTasksCompleted = Math.max(0, totalTasksCompleted);

        this.rerolls = Math.max(0, Math.min(MAX_REROLLS, rerolls));
        this.rerollProgress = Math.max(
                0,
                Math.min(TASKS_PER_REROLL - 1, rerollProgress)
        );

        this.rxp = Math.max(0, rxp);
        this.rollLevel = Math.max(
                1,
                Math.min(MAX_ROLL_LEVEL, rollLevel)
        );
        this.prestige = Math.max(0, prestige);

        this.nat1Count = Math.max(0, nat1Count);
        this.nat20Count = Math.max(0, nat20Count);

        this.bossingTasksCompleted = Math.max(0, bossingTasksCompleted);
        this.skillingTasksCompleted = Math.max(0, skillingTasksCompleted);
        this.makingTasksCompleted = Math.max(0, makingTasksCompleted);
        this.combatTasksCompleted = Math.max(0, combatTasksCompleted);
        this.adventureTasksCompleted = Math.max(0, adventureTasksCompleted);
        this.wildTasksCompleted = Math.max(0, wildTasksCompleted);

        this.equippedTitle =
                equippedTitle == null ? "" : equippedTitle;
    }
}
