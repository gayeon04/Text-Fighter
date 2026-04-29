package com.hotmail.kalebmarc.textfighter.enemy;

import com.hotmail.kalebmarc.textfighter.main.Enemy;

import java.util.*;
import java.util.stream.Collectors;


public class EnemyRegistry {

    private static final Map<String, EnemyFactory> registry = new LinkedHashMap<>();

    private static boolean initialized = false;


    public static void register(String name, EnemyFactory factory) {
        registry.put(name.toLowerCase(), factory);
    }

    public static Optional<Enemy> create(String name) {
        return Optional.ofNullable(registry.get(name.toLowerCase()))
                .map(EnemyFactory::create);
    }


    public static Optional<Enemy> createRandom() {
        if (registry.isEmpty()) return Optional.empty();
        List<String> keys = new ArrayList<>(registry.keySet());
        Collections.shuffle(keys);
        return create(keys.get(0));
    }


    public static List<String> getRegisteredNames() {
        return registry.keySet().stream()
                .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1))
                .collect(Collectors.toList());
    }


    public static int count() {
        return registry.size();
    }


    public static void initDefault() {
        if (initialized) return;
        initialized = true;

        register("zombie",      () -> new Enemy("Zombie",       20,  5, 10,  8, 14, 25,  1,  3, true, false));
        register("goblin",      () -> new Enemy("Goblin",       30,  8, 15, 10, 18, 35,  2,  5, true, false));
        register("ghost",       () -> new Enemy("Ghost",        25,  6, 12, 12, 20, 40,  3,  6, true, false));
        register("ninja",       () -> new Enemy("Ninja",        40, 12, 20, 15, 25, 55,  4,  7, true, false));
        register("darkElf",     () -> new Enemy("Dark Elf",     45, 15, 25, 18, 28, 65,  5,  8, true, false));
        register("barbarian",   () -> new Enemy("Barbarian",    55, 18, 30, 20, 32, 75,  6,  9, true, false));
        register("giantSpider", () -> new Enemy("Giant Spider", 35, 10, 18, 14, 22, 50,  3,  7, true, false));
        register("ogre",        () -> new Enemy("Ogre",         70, 20, 35, 25, 38, 90,  7, 10, true, false));

        register("dragon",      () -> new Enemy("Dragon",      100, 30, 50, 30, 45, 120, 8, 10, true, false));
        register("skeleton",    () -> new Enemy("Skeleton",     28,  7, 13, 11, 17, 38,  2,  5, true, false));
    }
}