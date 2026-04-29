package com.hotmail.kalebmarc.textfighter.quest;

/**
 * [Step 5] Observer Pattern - Observer 인터페이스
 *
 * QuestManager(Subject)가 이벤트 발생 시 이 인터페이스를 통해 알림.
 * Quest 클래스들이 이 인터페이스를 구현해서 이벤트를 수신.
 *
 * @FunctionalInterface 아님 - 메서드가 1개지만 상태를 가진 구현체가 필요해서.
 *
 * 적용 개념: Observer Pattern, 인터페이스
 */
public interface GameObserver {

    /**
     * 이벤트 수신 시 호출됨.
     *
     * @param event 발생한 이벤트 타입
     * @param data  이벤트 관련 데이터 (적 이름, 데미지 값 등)
     */
    void onEvent(GameEvent event, Object data);

    /**
     * 옵저버 이름 (로그/디버그용).
     */
    String getName();
}