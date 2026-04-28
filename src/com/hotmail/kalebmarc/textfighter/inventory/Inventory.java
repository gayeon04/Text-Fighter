package com.hotmail.kalebmarc.textfighter.inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Inventory<T extends Item> {

    private final List<T> items = new ArrayList<>();

    // ── 기본 CRUD ──────────────────────────────────────────

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