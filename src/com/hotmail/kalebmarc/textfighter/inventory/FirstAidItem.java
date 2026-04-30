package com.hotmail.kalebmarc.textfighter.inventory;

import com.hotmail.kalebmarc.textfighter.item.FirstAid;


public class FirstAidItem implements Item {

    @Override
    public String getName() {
        return "First-Aid Kit";
    }

    @Override
    public String getDescription() {
        return "Restores 20 health";
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
