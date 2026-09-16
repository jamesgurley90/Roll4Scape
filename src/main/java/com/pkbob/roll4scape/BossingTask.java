package com.pkbob.roll4scape;

public class BossingTask
{
    private final String bossName;
    private final int quantity;

    public BossingTask(String bossName, int quantity)
    {
        this.bossName = bossName;
        this.quantity = quantity;
    }

    public String getBossName()
    {
        return bossName;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public String getDisplayText()
    {
        return "Kill " + quantity + " " + bossName;
    }
}
