package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;

public class GreenTurtleTalismanRelic extends BaseRelic {
    private static final String NAME = "GreenTurtleTalisman";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int ENERGY = 1;
    private static final int DMG = 4;

    private boolean firstExhaust = false;

    public GreenTurtleTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atTurnStart(){
        firstExhaust = false;
    }

    @Override
    public void onExhaust(AbstractCard drawnCard) {
        if(!firstExhaust){
            this.flash();
            this.addToTop(new GainEnergyAction(ENERGY));
            this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, DMG, DamageInfo.DamageType.NORMAL)));
            firstExhaust = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[2] + DMG + DESCRIPTIONS[3];
    }
}
