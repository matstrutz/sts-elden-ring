package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eldenring.EldenRingSTS;

public class TakersCameoRelic extends BaseRelic {
    private static final String NAME = "TakersCameo";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;
    private static final int HEAL_QTD = 1;

    public TakersCameoRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(m, this));
            this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL_QTD, 0.0F));
        }
    }
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HEAL_QTD + DESCRIPTIONS[1];
    }
}
