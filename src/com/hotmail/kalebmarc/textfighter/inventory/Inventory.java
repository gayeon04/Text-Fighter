package com.hotmail.kalebmarc.textfighter.inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * [개선 전] 기존 코드 - 아이템마다 별도 static 변수로 관리:
 *
 *   // FirstAid.java
 *   private static int firstAid;
 *   public static int get() { return firstAid; }
 *   public static void set(int amount, boolean add) { ... }
 *
 *   // Potion.java
 *   private static int survivalPotion;
 *   private static int recoveryPotion;
 *   public static int get(String kind) { switch(kind) ... }
 *
 *   // InstaHealth.java, Power.java 도 동일한 패턴 반복
 *   → 아이템이 늘어날수록 클래스가 늘어남. 통합 관리 불가.
 *   → 수량 확인, 정렬, 검색을 각 클래스마다 따로 구현해야 함.
 *
 * [개선 후] Inventory<T extends Item> 하나로 통합:
 *   Inventory<PotionItem> potions = new Inventory<>();
 *   Inventory<FirstAidItem> kits  = new Inventory<>();
 *
 *   // Optional로 안전하게 꺼내기
 *   potions.findFirst(p -> p.getQuantity() > 0).ifPresent(Item::use);
 *
 *   // Stream으로 수량 요약
 *   potions.getSummary(); // {"생존 포션": 2, "회복 포션": 1}
 */

public class Inventory<T extends Item> {

    private final List<T> items = new ArrayList<>();


    public void add(T item) {
        items.add(item);
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }


    public Optional<T> findFirst(Predicate<T> condition) {
        return items.stream()
                .filter(condition)
                .findFirst();
    }


    public List<T> getSortedByQuantity() {
        return items.stream()
                .sorted(Comparator.comparingInt(T::getQuantity).reversed())
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getSummary() {
        return items.stream()
                .collect(Collectors.groupingBy(
                        Item::getName,
                        Collectors.summingInt(Item::getQuantity)
                ));
    }


    public int getTotalQuantity() {
        return items.stream()
                .mapToInt(Item::getQuantity)
                .sum();
    }


    public void printSummary() {
        if (items.isEmpty()) {
            System.out.println("  (인벤토리가 비어 있습니다)");
            return;
        }
        getSummary().forEach((name, qty) ->
                System.out.printf("  %-16s x%d%n", name, qty)
        );
    }

    public List<T> getAll() {
        return List.copyOf(items);
    }
}