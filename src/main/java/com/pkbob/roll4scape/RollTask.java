package com.pkbob.roll4scape;

public class RollTask
{
    private final String name;
    private final String action;
    private final boolean membersOnly;
    private final int minQuantity;
    private final int maxQuantity;

    public RollTask(String name, String action, boolean membersOnly, int minQuantity, int maxQuantity)
    {
        this.name = name;
        this.action = action;
        this.membersOnly = membersOnly;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    public String getName()
    {
        return name;
    }

    public String getAction()
    {
        return action;
    }

    public boolean isMembersOnly()
    {
        return membersOnly;
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
