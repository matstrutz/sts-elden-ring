package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eldenring.EldenRingSTS;

import java.util.Objects;

public class MillicentsProsthesisRelic extends BaseRelic {
    private static final String NAME = "MillicentsProsthesis";
    public static final String ID = EldenRingSTS.makeID(NAME);
    //TODO Move to special when event is done
    private static final RelicTier RARITY = RelicTier.SHOP;
    private static final LandingSound SOUND = LandingSound.CLINK;
    private static final int STR = 2;
    private static final int DEX = 1;
    private boolean firstTurn = true;
    private static AbstractCard lastCard;
    private static final int ATTACK_COUNT = 3;

    public MillicentsProsthesisRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
        if(AbstractDungeon.player.hasRelic(this.relicId)){
            if (this.firstTurn) {
                this.flash();
                this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, DEX), DEX));
                this.firstTurn = false;
            }
        }
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
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DEX + DESCRIPTIONS[1] + DESCRIPTIONS[2] + ATTACK_COUNT + DESCRIPTIONS[3] + STR + DESCRIPTIONS[4];
    }
}
