package com.hotmail.kalebmarc.textfighter.enemy;

import com.hotmail.kalebmarc.textfighter.main.Enemy;


@FunctionalInterface
public interface EnemyFactory {
    Enemy create();
}
