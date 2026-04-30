package com.hotmail.kalebmarc.textfighter.inventory;

import com.hotmail.kalebmarc.textfighter.player.Potion;


public class PotionItem implements Item {

    public enum PotionType {
        SURVIVAL("Survival Potion", "Restores 25% of max health"),
        RECOVERY("Recovery Potion", "Restores 75% of max health");
        private final String displayName;
        private final String description;

        PotionType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription()  { return description; }
    }

    private final PotionType type;

    public PotionItem(PotionType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type.getDisplayName();
    }

    @Override
    public String getDescription() {
        return type.getDescription();
    }

    @Override
    public int getQuantity() {
        return Potion.get(type.name().toLowerCase());
    }


    @Override
    public boolean use() {
        if (getQuantity() <= 0) return false;
        Potion.use(type.name().toLowerCase());
        return true;
    }

    public PotionType getType() {
        return type;
    }
}