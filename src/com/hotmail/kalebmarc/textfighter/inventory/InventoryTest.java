package com.hotmail.kalebmarc.textfighter.inventory;

import java.util.Optional;

public class InventoryTest {

    // ── 테스트용 더미 아이템 ──────────────────────────────

    static class DummyItem implements Item {
        private final String name;
        private final String desc;
        private int quantity;

        DummyItem(String name, String desc, int quantity) {
            this.name     = name;
            this.desc     = desc;
            this.quantity = quantity;
        }

        @Override public String getName()        { return name; }
        @Override public String getDescription() { return desc; }
        @Override public int    getQuantity()    { return quantity; }

        @Override
        public boolean use() {
            if (quantity <= 0) return false;
            quantity--;
            System.out.println("  [사용] " + name + " → 남은 수량: " + quantity);
            return true;
        }
    }

    // ── 테스트 실행 ──────────────────────────────────────

    public static void main(String[] args) {

        System.out.println("=== Step 1: Inventory<T> 테스트 ===\n");

        Inventory<DummyItem> bag = new Inventory<>();
        bag.add(new DummyItem("생존 포션",       "HP 25% 회복", 2));
        bag.add(new DummyItem("회복 포션",       "HP 75% 회복", 1));
        bag.add(new DummyItem("응급 치료 키트", "HP +20",       3));
        bag.add(new DummyItem("회복 포션",       "HP 75% 회복", 2)); // 중복 추가

        // ① 전체 수량
        System.out.println("① 전체 아이템 수량 합계: " + bag.getTotalQuantity());

        // ② 종류별 요약 (groupingBy + summingInt)
        System.out.println("\n② 종류별 수량 요약:");
        bag.printSummary();

        // ③ 수량 많은 순 정렬
        System.out.println("\n③ 수량 많은 순 정렬:");
        bag.getSortedByQuantity()
                .forEach(i -> System.out.printf("  %-16s x%d%n", i.getName(), i.getQuantity()));

        // ④ Optional - 수량 있는 회복 포션 찾아서 사용
        System.out.println("\n④ Optional - 회복 포션 찾아서 사용:");
        Optional<DummyItem> found = bag.findFirst(
                i -> i.getName().equals("회복 포션") && i.getQuantity() > 0
        );
        found.ifPresentOrElse(
                DummyItem::use,
                () -> System.out.println("  회복 포션이 없습니다!")
        );

        // ⑤ 없는 아이템 Optional 처리
        System.out.println("\n⑤ Optional - 없는 아이템 안전 처리:");
        bag.findFirst(i -> i.getName().equals("전설의 검"))
                .ifPresentOrElse(
                        DummyItem::use,
                        () -> System.out.println("  전설의 검이 없습니다! (NPE 없이 안전하게 처리됨)")
                );

        System.out.println("\n=== 테스트 완료 ===");
    }
}