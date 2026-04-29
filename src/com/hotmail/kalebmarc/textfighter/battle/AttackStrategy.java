package com.hotmail.kalebmarc.textfighter.battle;

// 기존 : game.java에 무기가 하드코딩 되어있음
// 개선 : 전투 전략을 인터페이스로 분리


// Strategy Pattern, 함수형 인터페이스
@FunctionalInterface
public interface AttackStrategy {

    int execute(int attackerDmgMin, int attackerDmgMax);
}