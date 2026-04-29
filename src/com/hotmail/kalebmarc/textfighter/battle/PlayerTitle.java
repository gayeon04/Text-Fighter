package com.hotmail.kalebmarc.textfighter.battle;

/**
 * [Step 4] 플레이어 칭호 enum
 *
 * 플레이 스타일에 따라 다른 칭호와 총평이 부여됨.
 * 문구는 직접 작성 → "내 프로젝트" 느낌의 핵심.
 *
 * 적용 개념: enum, OOP 심화
 */
public enum PlayerTitle {

    GLASS_CANNON(
            "🗡 유리 대포",
            "공격력은 최강인데 방어력은... 없습니다.\n" +
                    "    적보다 먼저 쓰러지면 의미가 없습니다."
    ),
    TANK(
            "🛡 맞으면서 이기는 스타일",
            "포션 소비량이 심상치 않습니다.\n" +
                    "    그래도 이긴 건 이긴 겁니다."
    ),
    COWARD(
            "🏃 도망의 미학",
            "도망치는 것도 전략입니다. 아마도.\n" +
                    "    살아있는 게 이기는 겁니다."
    ),
    PERFECTIONIST(
            "✨ 무결점 전사",
            "흠잡을 데가 없습니다.\n" +
                    "    좀비 상대로긴 하지만요."
    ),
    SNIPER_GOD(
            "🎯 저격의 신",
            "빗나간 적이 거의 없습니다.\n" +
                    "    당신은 저격총을 위해 태어났습니다."
    ),
    BERSERKER(
            "💢 버서커",
            "크리티컬이 남발됩니다.\n" +
                    "    분노가 전투력이 되는 타입입니다."
    ),
    POTION_ADDICT(
            "🧪 포션 중독자",
            "포션을 너무 많이 씁니다.\n" +
                    "    상점 사장이 당신을 좋아할 것 같습니다."
    ),
    LUCKY(
            "🍀 운빨 승리자",
            "실력인지 운인지 본인도 모를 것 같습니다.\n" +
                    "    하지만 이겼으니 됩니다."
    );

    private final String title;
    private final String comment;

    PlayerTitle(String title, String comment) {
        this.title   = title;
        this.comment = comment;
    }

    public String getTitle()   { return title;   }
    public String getComment() { return comment; }
}