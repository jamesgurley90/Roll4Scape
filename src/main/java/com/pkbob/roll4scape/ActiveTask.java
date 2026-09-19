package com.pkbob.roll4scape;

public class ActiveTask
{
    private final String category;
    private final String targetName;
    private final int requiredAmount;

    private int currentProgress;

    public ActiveTask(
            String category,
            String targetName,
            int requiredAmount)
    {
        this.category = category;
        this.targetName = targetName;
        this.requiredAmount = requiredAmount;
        this.currentProgress = 0;
    }
    public String getCategory()
    {
        return category;
    }

    public String getTargetName()
    {
        return targetName;
    }

    public int getRequiredAmount()
    {
        return requiredAmount;
    }

    public int getCurrentProgress()
    {
        return currentProgress;
    }

    public boolean isComplete()
    {
        return currentProgress >= requiredAmount;
    }

    public void addProgress()
    {
        if (currentProgress < requiredAmount)
        {
            currentProgress++;
        }
    }
    }