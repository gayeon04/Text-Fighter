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
            "Glass Cannon",
            "Max offense, zero defense.\n" +
                    "    Dying before the enemy does defeats the purpose."
    ),
    TANK(
            "Takes a Beating",
            "Your potion consumption is alarming.\n" +
                    "    But a win is a win."
    ),
    COWARD(
            "The Art of Running",
            "Running away is also a strategy. Probably.\n" +
                    "    Staying alive counts as winning."
    ),
    PERFECTIONIST(
            "Flawless Warrior",
            "Nothing to complain about.\n" +
                    "    Against zombies, but still."
    ),
    SNIPER_GOD(
            "Sniper God",
            "Barely a miss in sight.\n" +
                    "    You were born for the sniper rifle."
    ),
    BERSERKER(
            "Berserker",
            "Criticals everywhere.\n" +
                    "    Rage is your combat power."
    ),
    POTION_ADDICT(
            "Potion Addict",
            "You use way too many potions.\n" +
                    "    The shop owner loves you."
    ),
    LUCKY(
            "Lucky Winner",
            "Skill or luck? Even you don't know.\n" +
                    "    But you won, so it's fine."
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