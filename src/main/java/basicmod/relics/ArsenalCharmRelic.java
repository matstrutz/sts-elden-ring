package basicmod.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.PotionSlot;

import static basicmod.EldenRingSTS.makeID;

public class ArsenalCharmRelic extends BaseRelic {
    private static final String NAME = "ArsenalCharm";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;
    private static final int POTION_QTD = 1;

    public ArsenalCharmRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        AbstractPlayer abstractPlayer = AbstractDungeon.player;
        abstractPlayer.potionSlots += POTION_QTD;
        AbstractDungeon.player.potions.add(new PotionSlot(AbstractDungeon.player.potionSlots - POTION_QTD));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + POTION_QTD + DESCRIPTIONS[1];
    }
}
