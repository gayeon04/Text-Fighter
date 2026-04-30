package com.hotmail.kalebmarc.textfighter.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * [Step 6] AutoSaveTask - 멀티스레드 자동저장
 *
 * 기존 코드 문제:
 *   저장은 수동(case 10: Quit)이나 특정 조건에서만 호출됨.
 *   게임 중 예기치 않게 종료되면 데이터가 날아감.
 *
 * 개선:
 *   ScheduledExecutorService로 별도 스레드에서 N초마다 자동저장.
 *   메인 게임 루프를 전혀 방해하지 않음.
 *
 * 적용 개념:
 *   - 멀티스레드 기초 (ScheduledExecutorService)
 *   - AtomicInteger (스레드 안전한 카운터)
 *   - Runnable (람다로 작업 정의)
 *   - 스레드 생명주기 관리 (start/stop)
 */
public class AutoSaveTask {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    // 단일 스레드 스케줄러 - 저장 작업 전용
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "AutoSave-Thread");
                t.setDaemon(true); // 메인 스레드 종료 시 자동으로 같이 종료
                return t;
            });

    private ScheduledFuture<?> saveTask;

    // 스레드 안전한 저장 횟수 카운터
    private final AtomicInteger saveCount = new AtomicInteger(0);

    // 저장 콜백 (실제 저장 로직을 외부에서 주입 - 람다로)
    private final Runnable saveAction;

    // 저장 간격 (초)
    private final int intervalSeconds;

    /**
     * @param saveAction      실제 저장 로직 (람다로 주입)
     * @param intervalSeconds 저장 간격 (초)
     */
    public AutoSaveTask(Runnable saveAction, int intervalSeconds) {
        this.saveAction      = saveAction;
        this.intervalSeconds = intervalSeconds;
    }

    /**
     * 자동저장 시작.
     * initialDelay: 첫 저장까지 대기 시간
     * period: 이후 반복 간격
     */
    public void start() {
        saveTask = scheduler.scheduleAtFixedRate(
                this::doSave,          // 저장 작업 (메서드 레퍼런스)
                intervalSeconds,       // 첫 실행까지 대기
                intervalSeconds,       // 반복 간격
                TimeUnit.SECONDS
        );
        System.out.println("[AutoSave] Started - saving every " + intervalSeconds + " seconds");    }

    /**
     * 실제 저장 실행 (별도 스레드에서 호출됨).
     */
    private void doSave() {
        int count = saveCount.incrementAndGet();
        String time = LocalDateTime.now().format(FORMATTER);
        System.out.println("[AutoSave] Saving... (" + time + ") - #" + count);        try {
            saveAction.run(); // 외부에서 주입된 저장 로직 실행
            System.out.println("[AutoSave] Save completed");        } catch (Exception e) {
            System.out.println("[AutoSave] Save failed: " + e.getMessage());        }
    }

    /**
     * 자동저장 중지.
     * 게임 종료 시 반드시 호출해야 스레드가 정리됨.
     */
    public void stop() {
        if (saveTask != null) {
            saveTask.cancel(false); // 현재 실행 중인 작업은 완료 후 중지
        }
        scheduler.shutdown();
        System.out.println("[AutoSave] Stopped - total saves: " + saveCount.get());    }

    /**
     * 즉시 한 번 저장 (수동 트리거).
     */
    public void saveNow() {
        scheduler.execute(this::doSave);
    }

    public int getSaveCount() { return saveCount.get(); }
    public boolean isRunning() {
        return saveTask != null && !saveTask.isCancelled();
    }
}