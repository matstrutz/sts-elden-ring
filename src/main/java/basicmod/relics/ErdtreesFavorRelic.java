package basicmod.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.PotionSlot;

import static basicmod.EldenRingSTS.makeID;

public class ErdtreesFavorRelic extends BaseRelic {
    private static final String NAME = "ErdtreesFavor";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int ENERGY_START = 1;
    private static final int HP_AMOUNT = 4;
    private static final int POTION_QTD = 1;
    private boolean firstTurn = true;

    public ErdtreesFavorRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(HP_AMOUNT, true);

        AbstractDungeon.player.potionSlots += POTION_QTD;
        AbstractDungeon.player.potions.add(new PotionSlot(AbstractDungeon.player.potionSlots - POTION_QTD));
    }

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
    }

    @Override
    public void atTurnStart() {
        if (this.firstTurn) {
            this.flash();
            this.addToTop(new GainEnergyAction(ENERGY_START));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.firstTurn = false;
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1] + HP_AMOUNT + DESCRIPTIONS[2] + DESCRIPTIONS[3];
    }
}
