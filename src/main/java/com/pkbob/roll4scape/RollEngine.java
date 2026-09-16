package com.pkbob.roll4scape;

import java.util.Random;
import java.util.List;
import java.util.stream.Collectors;
public class RollEngine {
    private final Random random = new Random();
    private final List<BossingTask> bossingPool = List.of(
            new BossingTask("Giant Mole", "Kill", true, 1, 20),
            new BossingTask("Barrows chests", "Complete", true, 1, 10),
            new BossingTask("King Black Dragon", "Kill", true, 1, 15),
            new BossingTask("Obor", "Kill", false, 1, 10),
            new BossingTask("Bryophyta", "Kill", false, 1, 10)
    );
    private final List<RollTask> skillingPool = List.of(
            new RollTask("Woodcutting XP", "Gain", false, 500, 10000),
            new RollTask("Mining XP", "Gain", false, 500, 10000),
            new RollTask("Fishing XP", "Gain", false, 500, 10000),
            new RollTask("Agility XP", "Gain", true, 500, 10000),
            new RollTask("Thieving XP", "Gain", true, 500, 10000)
    );

    private final List<RollTask> makingPool = List.of(
            new RollTask("Shrimp", "Cook", false, 10, 100),
            new RollTask("Iron bars", "Smelt", false, 10, 100),
            new RollTask("Leather items", "Craft", false, 10, 100),
            new RollTask("Potions", "Make", true, 5, 50),
            new RollTask("Bows", "Fletch", true, 10, 100)
    );

    private final List<RollTask> combatPool = List.of(
            new RollTask("Goblins", "Kill", false, 5, 50),
            new RollTask("Cows", "Kill", false, 5, 50),
            new RollTask("Hill Giants", "Kill", false, 5, 50),
            new RollTask("Rock Crabs", "Kill", true, 10, 100),
            new RollTask("Slayer monsters", "Kill", true, 10, 100)
    );

    private final List<RollTask> adventurePool = List.of(
            new RollTask("Clue scroll", "Complete", false, 1, 3),
            new RollTask("Quest", "Complete", false, 1, 1),
            new RollTask("Achievement Diary task", "Complete", true, 1, 5),
            new RollTask("Minigame round", "Complete", true, 1, 5),
            new RollTask("New location", "Explore", false, 1, 3)
    );

    private final List<RollTask> wildPool = List.of(
            new RollTask("Goblin", "Kill", false, 1, 50),
            new RollTask("Shrimp", "Cook", false, 1, 100),
            new RollTask("Regular logs", "Chop", false, 5, 100),
            new RollTask("Cow", "Milk", false, 1, 10),
            new RollTask("Clue scroll", "Complete", false, 1, 3),
            new RollTask("Barrows chest", "Complete", true, 1, 10)
    );

    public int rollD20() {
        return random.nextInt(20) + 1;
    }

    public BossingTask generateBossingTask(int roll, boolean isMembers) {
        List<BossingTask> eligibleBosses = bossingPool.stream()
                .filter(boss -> isMembers || !boss.isMembersOnly())
                .collect(Collectors.toList());

        BossingTask boss = eligibleBosses.get(random.nextInt(eligibleBosses.size()));

        int min = boss.getMinQuantity();
        int max = boss.getMaxQuantity();

        int target = min + ((roll - 1) * (max - min) / 19);
        int variation = Math.max(1, (max - min) / 5);

        int low = Math.max(min, target - variation);
        int high = Math.min(max, target + variation);

        int quantity = low + random.nextInt(high - low + 1);

        return new BossingTask(
                boss.getBossName(),
                boss.getAction(),
                boss.isMembersOnly(),
                quantity,
                quantity
        );

    }

    public RollTask generateTask(List<RollTask> pool, int roll, boolean isMembers) {
        List<RollTask> eligibleTasks = pool.stream()
                .filter(task -> isMembers || !task.isMembersOnly())
                .collect(java.util.stream.Collectors.toList());

        RollTask task = eligibleTasks.get(random.nextInt(eligibleTasks.size()));

        int min = task.getMinQuantity();
        int max = task.getMaxQuantity();

        int target = min + ((roll - 1) * (max - min) / 19);
        int variation = Math.max(1, (max - min) / 5);

        int low = Math.max(min, target - variation);
        int high = Math.min(max, target + variation);

        int quantity = low + random.nextInt(high - low + 1);

        return new RollTask(
                task.getName(),
                task.getAction(),
                task.isMembersOnly(),
                quantity,
                quantity
        );
    }

    public List<RollTask> getSkillingPool() {
        return skillingPool;
    }
    public List<RollTask> getMakingPool()
    {
        return makingPool;
    }

    public List<RollTask> getCombatPool()
    {
        return combatPool;
    }

    public List<RollTask> getAdventurePool()
    {
        return adventurePool;
    }

    public List<RollTask> getWildPool()
    {
        return wildPool;
    }
}
