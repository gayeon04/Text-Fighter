package com.hotmail.kalebmarc.textfighter.battle;

/**
 * [Step 3] BattleManager + Strategy Pattern 테스트
 */
public class Battlemanagertest {

    public static void main(String[] args) {
        System.out.println("=== Step 3: BattleManager (Strategy Pattern) Test ===\n");

        System.out.println("1. Melee weapon strategy:");
        BattleManager manager = new BattleManager(AttackStrategies.MELEE);

        for (int i = 0; i < 3; i++) {
            int dmg = manager.attack(5, 10, "Fists");
            manager.takeDamage(8, "Zombie");
        }
        manager.printLog();

        System.out.println("\n2. Weapon changed -> Sniper strategy:");
        manager.reset();
        manager.setStrategy(AttackStrategies.SNIPER);

        for (int i = 0; i < 4; i++) {
            int dmg = manager.attack(20, 35, "Sniper");
            if (dmg > 0) manager.takeDamage(5, "Zombie");
        }
        manager.printLog();

        System.out.println("\n3. Aggressive bonus strategy (+5):");
        manager.reset();
        AttackStrategy aggressiveStrategy = AttackStrategies.withBonus(AttackStrategies.MELEE, 5);
        manager.setStrategy(aggressiveStrategy);

        for (int i = 0; i < 3; i++) {
            manager.attack(5, 10, "Fists (Aggressive)");
        }
        manager.printLog();

        System.out.println("\n4. Battle Statistics:");
        System.out.println("   Total damage dealt : " + manager.getTotalDamageDealt());
        System.out.println("   Total damage taken : " + manager.getTotalDamageTaken());
        System.out.println("   Total turns        : " + manager.getTurnsPlayed());
        System.out.println("   Critical hits      : " + manager.getCriticalHits());
        System.out.println("   Miss count         : " + manager.getMissCount());

        System.out.println("\n5. Custom strategy using lambda:");
        AttackStrategy berserker = (min, max) -> {
            int baseDmg = min + (int)(Math.random() * (max - min + 1));
            return (int)(baseDmg * 1.3);
        };
        manager.reset();
        manager.setStrategy(berserker);
        manager.attack(10, 20, "Berserker Fists");
        manager.printLog();

        System.out.println("\n=== Test Finished ===");
    }
}