package com.hotmail.kalebmarc.textfighter.quest;

/**
 * [Step 5] 게임 이벤트 타입 enum
 *
 * Observer Pattern에서 Subject가 발행하는 이벤트 종류.
 * 새 이벤트 추가 시 여기에만 추가하면 됨.
 *
 * 적용 개념: enum, Observer Pattern
 */
public enum GameEvent {
    ENEMY_KILLED,   // 적 처치
    ITEM_USED,      // 아이템 사용
    LEVEL_UP,       // 레벨업
    RAN_AWAY,       // 도망
    COINS_EARNED,   // 코인 획득
    DAMAGE_DEALT,   // 데미지
    CRITICAL_HIT    // 크리티컬
}
