package com.hotmail.kalebmarc.textfighter.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [Step 7] GameLogger - Singleton Pattern
 *
 * 게임 전체에서 하나의 인스턴스만 존재하는 로거.
 * AutoSaveTask(멀티스레드)에서도 안전하게 쓸 수 있도록
 * synchronized로 스레드 안전하게 구현.
 *
 * 적용 개념:
 *   - Singleton Pattern
 *   - 스레드 안전 (synchronized)
 *   - Stream (로그 필터/분석)
 *   - Collections.unmodifiableList
 */
public class GameLogger {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    // ── Singleton ────────────────────────────────────────
    private static volatile GameLogger instance; // volatile: 멀티스레드 가시성 보장

    private GameLogger() {}

    /** Double-checked locking Singleton (스레드 안전) */
    public static GameLogger getInstance() {
        if (instance == null) {
            synchronized (GameLogger.class) {
                if (instance == null) {
                    instance = new GameLogger();
                }
            }
        }
        return instance;
    }

    // ── 로그 저장소 ──────────────────────────────────────
    private final List<String> logs = new ArrayList<>();

    public enum LogLevel { INFO, WARN, ERROR, BATTLE, SAVE }

    /**
     * 로그 기록 (synchronized - 멀티스레드 안전).
     */
    public synchronized void log(LogLevel level, String message) {
        String entry = String.format("[%s][%s] %s",
                LocalDateTime.now().format(FORMATTER),
                level,
                message
        );
        logs.add(entry);
    }

    // 편의 메서드
    public void info  (String msg) { log(LogLevel.INFO,   msg); }
    public void warn  (String msg) { log(LogLevel.WARN,   msg); }
    public void error (String msg) { log(LogLevel.ERROR,  msg); }
    public void battle(String msg) { log(LogLevel.BATTLE, msg); }
    public void save  (String msg) { log(LogLevel.SAVE,   msg); }

    // ── Stream 분석 메서드 ────────────────────────────────

    /** 특정 레벨 로그만 필터 */
    public List<String> filterByLevel(LogLevel level) {
        return logs.stream()
                .filter(l -> l.contains("[" + level + "]"))
                .collect(Collectors.toList());
    }

    /** 키워드 포함 로그 검색 */
    public List<String> search(String keyword) {
        return logs.stream()
                .filter(l -> l.contains(keyword))
                .collect(Collectors.toList());
    }

    /** 전체 로그 출력 */
    public void printAll() {
        System.out.println("\n[GameLogger - 전체 로그 " + logs.size() + "건]");
        logs.forEach(System.out::println);
    }

    /** 레벨별 로그 수 요약 */
    public void printSummary() {
        System.out.println("\n[GameLogger 요약]");
        for (LogLevel level : LogLevel.values()) {
            long count = logs.stream()
                    .filter(l -> l.contains("[" + level + "]"))
                    .count();
            if (count > 0) {
                System.out.printf("  %-8s: %d건%n", level, count);
            }
        }
    }

    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    public int size() { return logs.size(); }
}