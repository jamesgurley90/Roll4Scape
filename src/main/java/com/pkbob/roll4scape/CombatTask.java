package com.pkbob.roll4scape;

public class CombatTask
{
    private final String name;
    private final boolean membersOnly;

    private final int minAttack;
    private final int minStrength;
    private final int minDefence;
    private final int minRanged;
    private final int minMagic;
    private final int minHitpoints;

    private final int slayerLevel;

    private final String requirementNote;
    private final boolean requiresSlayerTask;

    private final int minQuantity;
    private final int maxQuantity;

    public CombatTask(
            String name,
            boolean membersOnly,
            int minAttack,
            int minStrength,
            int minDefence,
            int minRanged,
            int minMagic,
            int minHitpoints,
            int slayerLevel,
            String requirementNote,
            boolean requiresSlayerTask,
            int minQuantity,
            int maxQuantity)
    {
        this.name = name;
        this.membersOnly = membersOnly;
        this.minAttack = minAttack;
        this.minStrength = minStrength;
        this.minDefence = minDefence;
        this.minRanged = minRanged;
        this.minMagic = minMagic;
        this.minHitpoints = minHitpoints;
        this.slayerLevel = slayerLevel;
        this.requirementNote = requirementNote;
        this.requiresSlayerTask = requiresSlayerTask;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    public String getName()
    {
        return name;
    }

    public boolean isMembersOnly()
    {
        return membersOnly;
    }

    public int getMinAttack()
    {
        return minAttack;
    }

    public int getMinStrength()
    {
        return minStrength;
    }

    public int getMinDefence()
    {
        return minDefence;
    }

    public int getMinRanged()
    {
        return minRanged;
    }

    public int getMinMagic()
    {
        return minMagic;
    }

    public int getMinHitpoints()
    {
        return minHitpoints;
    }

    public int getSlayerLevel()
    {
        return slayerLevel;
    }

    public String getRequirementNote()
    {
        return requirementNote;
    }

    public boolean requiresSlayerTask()
    {
        return requiresSlayerTask;
    }

    public int getMinQuantity()
    {
        return minQuantity;
    }

    public int getMaxQuantity()
    {
        return maxQuantity;
    }
}