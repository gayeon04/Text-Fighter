package com.hotmail.kalebmarc.textfighter.enemy;

import java.util.Optional;

/**
 * [Step 2] EnemyRegistry 동작 확인용 독립 테스트.
 * 기존 Enemy 클래스 의존성 없이 테스트하기 위해 더미 Enemy 사용.
 */
public class EnemyRegistryTest {

    // ── 테스트용 더미 Enemy ──────────────────────────────

    static class DummyEnemy {
        private final String name;
        private final int hp;
        private final int dmg;
        private final int xp;

        DummyEnemy(String name, int hp, int dmg, int xp) {
            this.name = name;
            this.hp   = hp;
            this.dmg  = dmg;
            this.xp   = xp;
        }

        public String getName() { return name; }
        public int getHp()      { return hp;   }
        public int getDmg()     { return dmg;  }
        public int getXp()      { return xp;   }

        @Override
        public String toString() {
            return String.format("%-12s HP:%-4d DMG:%-4d XP:%d", name, hp, dmg, xp);
        }
    }

    // ── 더미용 Factory/Registry ──────────────────────────

    @FunctionalInterface
    interface TestFactory {
        DummyEnemy create();
    }

    static java.util.Map<String, TestFactory> testRegistry = new java.util.LinkedHashMap<>();

    static void register(String name, TestFactory f) {
        testRegistry.put(name.toLowerCase(), f);
    }

    static Optional<DummyEnemy> create(String name) {
        return Optional.ofNullable(testRegistry.get(name.toLowerCase()))
                .map(TestFactory::create);
    }

    static Optional<DummyEnemy> createRandom() {
        java.util.List<String> keys = new java.util.ArrayList<>(testRegistry.keySet());
        java.util.Collections.shuffle(keys);
        return create(keys.get(0));
    }

    // ── 테스트 실행 ──────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Step 2: EnemyRegistry (Factory Pattern) 테스트 ===\n");

        // ① 적 등록 - 람다로 깔끔하게
        register("zombie",   () -> new DummyEnemy("좀비",    20,  8, 25));
        register("goblin",   () -> new DummyEnemy("고블린",  30, 12, 35));
        register("dragon",   () -> new DummyEnemy("드래곤", 100, 35, 120));
        register("skeleton", () -> new DummyEnemy("스켈레톤", 28, 11, 38));

        // ② 등록된 적 목록
        System.out.println("① 등록된 적 목록 (" + testRegistry.size() + "마리):");
        testRegistry.keySet().stream()
                .map(n -> n.substring(0,1).toUpperCase() + n.substring(1))
                .forEach(n -> System.out.println("   - " + n));

        // ③ 이름으로 생성 (Optional)
        System.out.println("\n② 이름으로 생성 - Optional 활용:");
        create("dragon").ifPresentOrElse(
                e -> System.out.println("   생성 성공: " + e),
                ()  -> System.out.println("   없는 적!")
        );

        // ④ 없는 적 안전 처리
        System.out.println("\n③ 없는 적 안전 처리:");
        create("phoenix").ifPresentOrElse(
                e -> System.out.println("   " + e),
                ()  -> System.out.println("   phoenix 없음 (NPE 없이 안전하게 처리됨)")
        );

        // ⑤ 랜덤 생성 3회
        System.out.println("\n④ 랜덤 적 생성 3회:");
        for (int i = 0; i < 3; i++) {
            createRandom().ifPresent(e ->
                    System.out.println("   → " + e)
            );
        }

        // ⑥ 신규 적 추가가 얼마나 쉬운지 시연
        System.out.println("\n⑤ 신규 적 추가 (한 줄):");
        register("phoenix", () -> new DummyEnemy("불사조", 80, 28, 100));
        System.out.println("   등록 후 총 적 수: " + testRegistry.size() + "마리");
        create("phoenix").ifPresent(e -> System.out.println("   " + e));

        System.out.println("\n=== 테스트 완료 ===");
    }
}