package eldenring.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static eldenring.EldenRingSTS.makeID;

public class TwinbladeTalismanRelic extends BaseRelic {
    private static final String NAME = "TwinbladeTalisman";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int BASE_DMG = 10;
    private static AbstractCard lastCard;

    public TwinbladeTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(this.counter == 0){
            ++this.counter;
            lastCard = card;
        } else {
            if (card.cardID.equals(lastCard.cardID)) {
                ++this.counter;
                if (this.counter >= 3) {
                    this.flash();
                    this.counter = 0;
                    this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    this.addToBot(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(BASE_DMG, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }
            } else {
                this.counter = 0;
                lastCard = null;
            }
        }
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BASE_DMG + DESCRIPTIONS[1];
    }
}
