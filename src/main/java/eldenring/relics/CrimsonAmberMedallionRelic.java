package eldenring.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;

public class CrimsonAmberMedallionRelic extends BaseRelic {
    private static final String NAME = "CrimsonAmberMedallion";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;
    private static final int HP_AMOUNT = 10;

    public CrimsonAmberMedallionRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(HP_AMOUNT, true);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HP_AMOUNT;
    }
}
