package com.hotmail.kalebmarc.textfighter.inventory;

/**
 * [Step 1] Item 인터페이스
 *
 * 기존 코드의 Weapon, Armour, Potion, FirstAid는 공통 타입 없이 따로 관리됐음.
 * Item 인터페이스로 묶어 Inventory<T>에서 제네릭으로 다룰 수 있게 함.
 *
 * 적용 개념: 인터페이스, OOP 심화 (다형성)
 */
public interface Item {

    /** 아이템 이름 반환 */
    String getName();

    /** 아이템 설명 반환 */
    String getDescription();

    /** 현재 보유 수량 반환 */
    int getQuantity();

    /** 아이템 사용 (성공 여부 반환) */
    boolean use();
}
