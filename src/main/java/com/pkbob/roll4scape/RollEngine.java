package com.pkbob.roll4scape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import net.runelite.api.Client;
import net.runelite.api.Skill;

public class RollEngine
{
    private final Random random = new Random();

    /*
     * -------------------------
     * BOSSING
     * -------------------------
     *
     * Bossing is still using the simpler BossingTask system for now.
     * We'll give Bossing the same account-aware treatment later.
     */
    private final List<BossingTask> bossingPool = List.of(
            new BossingTask("Giant Mole", "Kill", true, 1, 20),
            new BossingTask("Barrows chests", "Complete", true, 1, 10),
            new BossingTask("King Black Dragon", "Kill", true, 1, 15),
            new BossingTask("Obor", "Kill", false, 1, 10),
            new BossingTask("Bryophyta", "Kill", false, 1, 10)
    );

    /*
     * -------------------------
     * SKILLING
     * -------------------------
     *
     * These are still prototype pools.
     * Combat is the category we're upgrading first.
     */
    private final List<RollTask> skillingPool = List.of(
            new RollTask("Woodcutting XP", "Gain", false, 500, 10000),
            new RollTask("Mining XP", "Gain", false, 500, 10000),
            new RollTask("Fishing XP", "Gain", false, 500, 10000),
            new RollTask("Agility XP", "Gain", true, 500, 10000),
            new RollTask("Thieving XP", "Gain", true, 500, 10000)
    );

    /*
     * -------------------------
     * MAKING
     * -------------------------
     */
    private final List<RollTask> makingPool = List.of(
            new RollTask("Shrimp", "Cook", false, 10, 100),
            new RollTask("Iron bars", "Smelt", false, 10, 100),
            new RollTask("Leather items", "Craft", false, 10, 100),
            new RollTask("Potions", "Make", true, 5, 50),
            new RollTask("Bows", "Fletch", true, 10, 100)
    );

    /*
     * -------------------------
     * COMBAT
     * -------------------------
     *
     * Format:
     *
     * name,
     * membersOnly,
     * minAttack,
     * minStrength,
     * minDefence,
     * minRanged,
     * minMagic,
     * minHitpoints,
     * slayerLevel,
     * requirementNote,
     * requiresSlayerTask,
     * minKills,
     * maxKills
     *
     * IMPORTANT:
     * These stat values are Roll4Scape balancing thresholds.
     * They are NOT claims that OSRS literally requires these
     * combat stats to attack the monster.
     *
     * A player can qualify through melee, ranged, OR magic.
     */
    private final List<CombatTask> combatPool = List.of(

            // ---------- F2P: VERY LOW ----------
            new CombatTask("Chicken", false,
                    1, 1, 1, 1, 1, 10,
                    1, "", false, 5, 100),

            new CombatTask("Cow", false,
                    1, 1, 1, 1, 1, 10,
                    1, "", false, 5, 100),

            new CombatTask("Goblin", false,
                    1, 1, 1, 1, 1, 10,
                    1, "", false, 5, 100),

            new CombatTask("Giant rat", false,
                    1, 1, 1, 1, 1, 10,
                    1, "", false, 5, 100),

            new CombatTask("Imp", false,
                    1, 1, 1, 1, 1, 10,
                    1, "", false, 5, 80),

            new CombatTask("Spider", false,
                    1, 1, 1, 1, 1, 10,
                    1, "", false, 5, 100),

            // ---------- F2P: LOW ----------
            new CombatTask("Barbarian", false,
                    10, 10, 1, 10, 10, 10,
                    1, "", false, 5, 80),

            new CombatTask("Al Kharid warrior", false,
                    10, 10, 1, 10, 10, 10,
                    1, "", false, 5, 80),

            new CombatTask("Guard", false,
                    15, 15, 1, 15, 15, 15,
                    1, "", false, 5, 80),

            new CombatTask("Dwarf", false,
                    10, 10, 1, 10, 10, 10,
                    1, "", false, 5, 80),

            new CombatTask("Dark wizard", false,
                    15, 15, 1, 15, 15, 15,
                    1, "", false, 5, 70),

            new CombatTask("Minotaur", false,
                    15, 15, 1, 15, 15, 15,
                    1, "", false, 5, 80),

            new CombatTask("Scorpion", false,
                    20, 20, 1, 20, 20, 20,
                    1, "", false, 5, 70),

            new CombatTask("Skeleton", false,
                    15, 15, 1, 15, 15, 15,
                    1, "", false, 5, 80),

            new CombatTask("Zombie", false,
                    15, 15, 1, 15, 15, 15,
                    1, "", false, 5, 80),

            new CombatTask("Ghost", false,
                    15, 15, 1, 15, 15, 15,
                    1, "", false, 5, 70),

            // ---------- F2P: MEDIUM ----------
            new CombatTask("Flesh crawler", false,
                    25, 25, 1, 25, 25, 20,
                    1, "", false, 5, 70),

            new CombatTask("Hobgoblin", false,
                    25, 25, 1, 25, 25, 20,
                    1, "", false, 5, 70),

            new CombatTask("Hill giant", false,
                    30, 30, 1, 30, 30, 25,
                    1, "", false, 5, 60),

            new CombatTask("Moss giant", false,
                    35, 35, 1, 35, 35, 30,
                    1, "", false, 5, 55),

            new CombatTask("Ice warrior", false,
                    35, 35, 1, 35, 35, 30,
                    1, "", false, 5, 50),

            new CombatTask("Ice giant", false,
                    40, 40, 1, 40, 40, 35,
                    1, "", false, 5, 50),

            new CombatTask("Black Knight", false,
                    25, 25, 1, 25, 25, 20,
                    1, "", false, 5, 60),

            // ---------- F2P: HIGHER ----------
            new CombatTask("Lesser demon", false,
                    45, 45, 1, 45, 45, 40,
                    1, "", false, 3, 40),

            new CombatTask("Greater demon", false,
                    50, 50, 1, 50, 50, 45,
                    1, "", false, 3, 35),

            new CombatTask("Ankou", false,
                    45, 45, 1, 45, 45, 40,
                    1, "", false, 3, 40),

            new CombatTask("Ogress Warrior", false,
                    50, 50, 1, 50, 50, 45,
                    1, "", false, 3, 35),

            new CombatTask("Ogress Shaman", false,
                    50, 50, 1, 50, 50, 45,
                    1, "", false, 3, 35),

            // ---------- MEMBERS: GENERAL ----------
            new CombatTask("Rock crab", true,
                    10, 10, 1, 10, 10, 10,
                    1, "", false, 10, 120),

            new CombatTask("Sand crab", true,
                    15, 15, 1, 15, 15, 15,
                    1, "", false, 10, 120),

            new CombatTask("Ammonite crab", true,
                    20, 20, 1, 20, 20, 20,
                    1, "Fossil Island access required.", false, 10, 120),

            new CombatTask("Chaos druid", true,
                    20, 20, 1, 20, 20, 20,
                    1, "", false, 5, 80),

            new CombatTask("Jogre", true,
                    25, 25, 1, 25, 25, 20,
                    1, "", false, 5, 70),

            new CombatTask("Ogre", true,
                    30, 30, 1, 30, 30, 25,
                    1, "", false, 5, 70),

            new CombatTask("Fire giant", true,
                    45, 45, 1, 45, 45, 40,
                    1, "", false, 5, 50),

            new CombatTask("Dagannoth", true,
                    45, 45, 1, 45, 45, 40,
                    1, "", false, 5, 50),

            new CombatTask("Green dragon", true,
                    45, 45, 1, 45, 45, 40,
                    1, "Use a non-Wilderness location for Combat rolls.", false, 3, 35),

            new CombatTask("Blue dragon", true,
                    50, 50, 1, 50, 50, 45,
                    1, "", false, 3, 30),

            new CombatTask("Red dragon", true,
                    55, 55, 1, 55, 55, 50,
                    1, "", false, 3, 25),

            new CombatTask("Black dragon", true,
                    60, 60, 1, 60, 60, 55,
                    1, "", false, 2, 20),

            new CombatTask("Kalphite Worker", true,
                    25, 25, 1, 25, 25, 20,
                    1, "", false, 5, 70),

            new CombatTask("Kalphite Soldier", true,
                    40, 40, 1, 40, 40, 35,
                    1, "", false, 5, 50),

            new CombatTask("Kalphite Guardian", true,
                    55, 55, 1, 55, 55, 50,
                    1, "", false, 3, 30),

            new CombatTask("TzHaar-Ket", true,
                    55, 55, 1, 55, 55, 50,
                    1, "", false, 3, 30),

            new CombatTask("TzHaar-Xil", true,
                    55, 55, 1, 55, 55, 50,
                    1, "", false, 3, 30),

            // ---------- MEMBERS: SLAYER ----------
            new CombatTask("Banshee", true,
                    20, 20, 1, 20, 20, 15,
                    15, "Earmuffs are required.", false, 5, 70),

            new CombatTask("Rockslug", true,
                    25, 25, 1, 25, 25, 20,
                    20, "Bring a bag of salt.", false, 5, 60),

            new CombatTask("Cockatrice", true,
                    30, 30, 1, 30, 30, 25,
                    25, "A mirror shield is required.", false, 5, 60),

            new CombatTask("Pyrefiend", true,
                    30, 30, 1, 30, 30, 25,
                    30, "", false, 5, 60),

            new CombatTask("Mogre", true,
                    35, 35, 1, 35, 35, 30,
                    32, "Mogre access requirements apply.", false, 5, 50),

            new CombatTask("Harpie Bug Swarm", true,
                    35, 35, 1, 35, 35, 30,
                    33, "A lit bug lantern is required.", false, 5, 50),

            new CombatTask("Wall beast", true,
                    35, 35, 1, 35, 35, 30,
                    35, "A spiny helmet or equivalent protection is recommended.", false, 5, 50),

            new CombatTask("Killerwatt", true,
                    40, 40, 1, 40, 40, 35,
                    37, "Creature of Fenkenstrain progression is required.", false, 5, 45),

            new CombatTask("Basilisk", true,
                    40, 40, 1, 40, 40, 35,
                    40, "A mirror shield is required.", false, 5, 45),

            new CombatTask("Fever spider", true,
                    40, 40, 1, 40, 40, 35,
                    42, "Rum Deal access requirements apply.", false, 5, 45),

            new CombatTask("Infernal Mage", true,
                    40, 40, 1, 40, 40, 35,
                    45, "", false, 5, 45),

            new CombatTask("Bloodveld", true,
                    45, 45, 1, 45, 45, 40,
                    50, "", false, 5, 40),

            new CombatTask("Jelly", true,
                    45, 45, 1, 45, 45, 40,
                    52, "", false, 5, 40),

            new CombatTask("Turoth", true,
                    50, 50, 1, 50, 50, 45,
                    55, "Use appropriate Slayer equipment or attacks.", false, 5, 35),

            new CombatTask("Cave horror", true,
                    50, 50, 1, 50, 50, 45,
                    58, "Mos Le'Harmless access and a witchwood icon are required.", false, 5, 35),

            new CombatTask("Aberrant spectre", true,
                    55, 55, 1, 55, 55, 50,
                    60, "Nose protection is required.", false, 5, 35),

            new CombatTask("Dust devil", true,
                    55, 55, 1, 55, 55, 50,
                    65, "A face mask or equivalent protection is required.", false, 5, 35),

            new CombatTask("Kurask", true,
                    60, 60, 1, 60, 60, 55,
                    70, "Use appropriate Slayer equipment or attacks.", false, 3, 30),

            new CombatTask("Gargoyle", true,
                    60, 60, 1, 60, 60, 55,
                    75, "A rock hammer is needed to finish the kill.", false, 3, 30),

            new CombatTask("Nechryael", true,
                    65, 65, 1, 65, 65, 60,
                    80, "", false, 3, 25),

            new CombatTask("Abyssal demon", true,
                    70, 70, 1, 70, 70, 65,
                    85, "", false, 3, 25),

            new CombatTask("Cave kraken", true,
                    70, 70, 1, 70, 70, 65,
                    87, "An active cave kraken or Kraken Slayer assignment is required.", true, 3, 20),

            new CombatTask("Dark beast", true,
                    75, 75, 1, 75, 75, 70,
                    90, "Mourning's End Part II access requirements apply.", false, 3, 20),

            new CombatTask("Smoke devil", true,
                    75, 75, 1, 75, 75, 70,
                    93, "A Slayer helmet, face mask, or equivalent protection is required.", false, 3, 20),

            new CombatTask("Hydra", true,
                    80, 80, 1, 80, 80, 75,
                    95, "Karuulm Slayer Dungeon access requirements apply.", false, 2, 15)
    );

    /*
     * -------------------------
     * ADVENTURE
     * -------------------------
     */
    private final List<RollTask> adventurePool = List.of(
            new RollTask("Clue scroll", "Complete", false, 1, 3),
            new RollTask("Quest", "Complete", false, 1, 1),
            new RollTask("Achievement Diary task", "Complete", true, 1, 5),
            new RollTask("Minigame round", "Complete", true, 1, 5),
            new RollTask("New location", "Explore", false, 1, 3)
    );

    /*
     * -------------------------
     * WILD
     * -------------------------
     *
     * This is still the small prototype pool.
     * Later Wild will be built from all categories plus
     * Wilderness and special Roll4Scape tasks.
     */
    private final List<RollTask> wildPool = List.of(
            new RollTask("Goblin", "Kill", false, 1, 50),
            new RollTask("Shrimp", "Cook", false, 1, 100),
            new RollTask("Regular logs", "Chop", false, 5, 100),
            new RollTask("Cow", "Milk", false, 1, 10),
            new RollTask("Clue scroll", "Complete", false, 1, 3),
            new RollTask("Barrows chest", "Complete", true, 1, 10)
    );

    public int rollD20()
    {
        return random.nextInt(20) + 1;
    }

    /*
     * -------------------------
     * BOSS GENERATION
     * -------------------------
     */
    public BossingTask generateBossingTask(int roll, boolean isMembers)
    {
        List<BossingTask> eligibleBosses = bossingPool.stream()
                .filter(task -> isMembers || !task.isMembersOnly())
                .collect(Collectors.toList());

        if (eligibleBosses.isEmpty())
        {
            return null;
        }

        BossingTask boss =
                eligibleBosses.get(random.nextInt(eligibleBosses.size()));

        int quantity = generateQuantity(
                roll,
                boss.getMinQuantity(),
                boss.getMaxQuantity()
        );

        return new BossingTask(
                boss.getBossName(),
                boss.getAction(),
                boss.isMembersOnly(),
                quantity,
                quantity
        );
    }

    /*
     * -------------------------
     * GENERIC TASK GENERATION
     * -------------------------
     */
    public RollTask generateTask(
            List<RollTask> pool,
            int roll,
            boolean isMembers)
    {
        List<RollTask> eligibleTasks = pool.stream()
                .filter(task -> isMembers || !task.isMembersOnly())
                .collect(Collectors.toList());

        if (eligibleTasks.isEmpty())
        {
            return null;
        }

        RollTask task =
                eligibleTasks.get(random.nextInt(eligibleTasks.size()));

        int quantity = generateQuantity(
                roll,
                task.getMinQuantity(),
                task.getMaxQuantity()
        );

        return new RollTask(
                task.getName(),
                task.getAction(),
                task.isMembersOnly(),
                quantity,
                quantity
        );
    }

    /*
     * -------------------------
     * COMBAT TASK GENERATION
     * -------------------------
     */
    public CombatTask generateCombatTask(
            Client client,
            int roll,
            boolean isMembers)
    {
        List<CombatTask> eligibleTasks = new ArrayList<>();

        for (CombatTask task : combatPool)
        {
            if (isCombatTaskEligible(client, task, isMembers))
            {
                eligibleTasks.add(task);
            }
        }

        if (eligibleTasks.isEmpty())
        {
            return null;
        }

        CombatTask task =
                eligibleTasks.get(random.nextInt(eligibleTasks.size()));

        int quantity = generateQuantity(
                roll,
                task.getMinQuantity(),
                task.getMaxQuantity()
        );

        /*
         * NAT 1 is intentionally tiny.
         *
         * Later we'll replace the ordinary eligible pool with a
         * dedicated funny/fumble pool for Nat 1.
         */
        if (roll == 1)
        {
            quantity = 1;
        }

        return new CombatTask(
                task.getName(),
                task.isMembersOnly(),
                task.getMinAttack(),
                task.getMinStrength(),
                task.getMinDefence(),
                task.getMinRanged(),
                task.getMinMagic(),
                task.getMinHitpoints(),
                task.getSlayerLevel(),
                task.getRequirementNote(),
                task.requiresSlayerTask(),
                quantity,
                quantity
        );
    }

    /*
     * Determines whether a Combat task is reasonable for the account.
     *
     * This does NOT use RuneScape's displayed combat level.
     * It checks the player's actual combat skills.
     */
    private boolean isCombatTaskEligible(
            Client client,
            CombatTask task,
            boolean isMembers)
    {
        if (task.isMembersOnly() && !isMembers)
        {
            return false;
        }

        int attack = client.getRealSkillLevel(Skill.ATTACK);
        int strength = client.getRealSkillLevel(Skill.STRENGTH);
        int defence = client.getRealSkillLevel(Skill.DEFENCE);
        int ranged = client.getRealSkillLevel(Skill.RANGED);
        int magic = client.getRealSkillLevel(Skill.MAGIC);
        int hitpoints = client.getRealSkillLevel(Skill.HITPOINTS);
        int slayer = client.getRealSkillLevel(Skill.SLAYER);

        if (hitpoints < task.getMinHitpoints())
        {
            return false;
        }

        if (slayer < task.getSlayerLevel())
        {
            return false;
        }

        /*
         * Three possible combat routes.
         *
         * MELEE:
         * Attack + Strength meet the balancing threshold.
         *
         * RANGED:
         * Ranged meets the balancing threshold.
         *
         * MAGIC:
         * Magic meets the balancing threshold.
         *
         * Defence is deliberately checked separately below so
         * low-defence builds are not automatically excluded.
         */
        boolean meleeReady =
                attack >= task.getMinAttack()
                        && strength >= task.getMinStrength();

        boolean rangedReady =
                ranged >= task.getMinRanged();

        boolean magicReady =
                magic >= task.getMinMagic();

        if (!meleeReady && !rangedReady && !magicReady)
        {
            return false;
        }

        /*
         * Defence is currently a SOFT requirement.
         *
         * We store it because it will be useful for later balancing,
         * but we don't reject accounts solely because Defence is low.
         *
         * This allows reasonable pure/ranged/magic builds to qualify.
         */
        return true;
    }

    /*
     * -------------------------
     * D20 QUANTITY SCALING
     * -------------------------
     *
     * 1  = minimum/fumble territory
     * 20 = maximum/Nat 20 territory
     *
     * The small random variation prevents the same roll from always
     * producing exactly the same quantity.
     */
    private int generateQuantity(
            int roll,
            int min,
            int max)
    {
        if (max <= min)
        {
            return min;
        }

        if (roll <= 1)
        {
            return min;
        }

        if (roll >= 20)
        {
            return max;
        }

        int target =
                min + ((roll - 1) * (max - min) / 19);

        int variation =
                Math.max(1, (max - min) / 5);

        int low =
                Math.max(min, target - variation);

        int high =
                Math.min(max, target + variation);

        return low + random.nextInt(high - low + 1);
    }

    public List<RollTask> getSkillingPool()
    {
        return skillingPool;
    }

    public List<RollTask> getMakingPool()
    {
        return makingPool;
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