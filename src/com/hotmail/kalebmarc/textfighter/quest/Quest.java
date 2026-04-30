package com.hotmail.kalebmarc.textfighter.quest;

/**
 * [Step 5] Quest 추상 클래스
 *
 * 모든 퀘스트의 공통 구조 정의.
 * 구체적인 달성 조건은 하위 클래스에서 구현 (Template Method 패턴 혼합).
 *
 * 적용 개념: 추상 클래스, OOP 심화, Observer Pattern
 */
public abstract class Quest implements GameObserver {

    protected final String title;
    protected final String description;
    protected final int    rewardCoins;
    protected boolean      completed = false;

    protected Quest(String title, String description, int rewardCoins) {
        this.title       = title;
        this.description = description;
        this.rewardCoins = rewardCoins;
    }

    /**
     * 퀘스트 달성 시 호출.
     * 하위 클래스에서 조건 달성 시 super.complete() 호출.
     */
    protected void complete() {
        if (completed) return;
        completed = true;
        System.out.println("\nQuest Complete: [" + title + "]");
        System.out.println("   " + description);
        System.out.println("   Reward: " + rewardCoins + " coins"); }

    public boolean isCompleted()   { return completed;    }
    public String  getTitle()      { return title;        }
    public String  getDescription(){ return description;  }
    public int     getReward()     { return rewardCoins;  }

    @Override
    public String getName() { return title; }
}