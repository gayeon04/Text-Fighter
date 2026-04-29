package com.hotmail.kalebmarc.textfighter.battle;

import java.util.Random;

/**
 * [Step 3] 전략 구현체 모음 클래스
 *
 * 각 전략은 static final 상수로 정의.
 * AttackStrategy가 @FunctionalInterface라 람다로 간결하게 표현.
 *
 * 적용 개념: Strategy Pattern, 람다, static factory
 */
public class AttackStrategies {

    private static final Random random = new Random();

    /**
     * 기본 근접 공격 - 범위 내 랜덤 데미지.
     * 기존 코드의 일반 전투 로직과 동일.
     */
    public static final AttackStrategy MELEE = (min, max) -> {
        if (max <= min) return min;
        return min + random.nextInt(max - min + 1);
    };

    /**
     * 저격 전략 - 70% 확률로 명중, 30% 빗나감.
     * 기존 코드: if (fightPath <= 30) Enemy.get().dealDamage(); 를 전략으로 추출.
     */
    public static final AttackStrategy SNIPER = (min, max) -> {
        if (random.nextInt(100) < 30) {
            return 0; // 빗나감
        }
        int range = (max > min) ? max - min + 1 : 1;
        return (int) ((min + random.nextInt(range)) * 1.5);
    };

    /**
     * 산탄총 전략 - 낮은 데미지지만 반드시 명중 + 추가타 확률.
     */
    public static final AttackStrategy SHOTGUN = (min, max) -> {
        int range = (max > min) ? max - min + 1 : 1;
        int base = min + random.nextInt(range);
        int pellets = 2 + random.nextInt(3); // 2~4발
        return base * pellets / 3;
    };

    /**
     * 크리티컬 전략 - 20% 확률로 2배 데미지.
     * 람다 안에서 조건 분기 시연.
     */
    public static final AttackStrategy CRITICAL = (min, max) -> {
        int base = min + random.nextInt(max - min + 1);
        return random.nextInt(100) < 20 ? base * 2 : base;
    };

    /**
     * 성향 시스템 연동 - 공격적 성향이면 보너스 데미지 추가.
     * 람다를 반환하는 팩토리 메서드 (고차 함수 시연).
     *
     * @param bonusDmg 성향 보너스 데미지
     */
    public static AttackStrategy withBonus(AttackStrategy base, int bonusDmg) {
        return (min, max) -> base.execute(min, max) + bonusDmg;
    }

    private AttackStrategies() {} // 인스턴스 생성 방지
}