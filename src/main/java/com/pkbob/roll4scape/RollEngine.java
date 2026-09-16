package com.pkbob.roll4scape;

import java.util.Random;
import java.util.List;
public class RollEngine
{
    private final Random random = new Random();
    private final List<String> bossingPool = List.of(
            "Giant Mole",
            "Barrows",
            "King Black Dragon"
    );
    public int rollD20()
    {
        return random.nextInt(20) + 1;
    }public BossingTask generateBossingTask(int roll)
{
    String boss = bossingPool.get(random.nextInt(bossingPool.size()));

    int quantity = roll;

    return new BossingTask(boss, quantity);
}
}
