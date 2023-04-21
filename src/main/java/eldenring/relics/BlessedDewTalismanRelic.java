package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;

public class BlessedDewTalismanRelic extends BaseRelic {
    private static final String NAME = "BlessedDewTalisman";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;
    private static final int HEAL_QTD = 2;

    public BlessedDewTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL_QTD, 0.0F));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HEAL_QTD + DESCRIPTIONS[1];
    }
}
