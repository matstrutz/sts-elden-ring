package eldenring.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public abstract class BasePotion extends CustomPotion {
    public BasePotion(String name, String id, PotionRarity rarity, PotionSize size, PotionColor color) {
        super(name, id, rarity, size, color);
    }

    public AbstractPotion makeCopy() {
        try {
            return this.getClass().newInstance();
        } catch (IllegalAccessException | InstantiationException var2) {
            throw new RuntimeException("BaseMod failed to auto-generate makeCopy for relic: " + this.ID);
        }
    }
}
