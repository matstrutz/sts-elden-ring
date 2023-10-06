package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eldenring.EldenRingSTS;

import java.util.Objects;

public class RottenWingedSwordInsigniaRelic extends BaseRelic {
    private static final String NAME = "RottenWingedSwordInsignia";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;
    private static final int STR = 2;
    private static AbstractCard lastCard;
    private static final int ATTACK_COUNT = 3;

    public RottenWingedSwordInsigniaRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction useCardAction) {
        if (this.counter == 0) {
            if(AbstractCard.CardType.ATTACK.equals(card.type)){
                ++this.counter;
                lastCard = card;
            }
        } else {
            if (AbstractCard.CardType.ATTACK.equals(card.type)) {
                if (card.cost == lastCard.cost) {
                    ++this.counter;
                    if (this.counter >= ATTACK_COUNT) {
                        this.flash();
                        this.counter = 0;
                        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STR), STR));
                    }
                } else {
                    this.counter = 0;
                    lastCard = null;
                }
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
        return DESCRIPTIONS[0] + ATTACK_COUNT + DESCRIPTIONS[1] + STR + DESCRIPTIONS[2];
    }
}
