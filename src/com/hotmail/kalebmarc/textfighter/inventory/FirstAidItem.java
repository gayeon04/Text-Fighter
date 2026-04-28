package com.hotmail.kalebmarc.textfighter.inventory;

import com.hotmail.kalebmarc.textfighter.item.FirstAid;


public class FirstAidItem implements Item {

    @Override
    public String getName() {
        return "응급 치료 키트";
    }

    @Override
    public String getDescription() {
        return "체력 20 회복";
    }

    @Override
    public int getQuantity() {
        return FirstAid.get();
    }

    @Override
    public boolean use() {
        if (getQuantity() <= 0) return false;
        FirstAid.use();
        return true;
    }
}
