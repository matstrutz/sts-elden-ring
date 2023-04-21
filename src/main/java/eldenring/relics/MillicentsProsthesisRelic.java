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
    //TODO Move do special um event is done
    private static final RelicTier RARITY = RelicTier.SHOP;
    private static final LandingSound SOUND = LandingSound.CLINK;
    private static final int STR = 2;
    private static final int DEX = 2;
    private boolean firstTurn = true;

    public MillicentsProsthesisRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
    }

    @Override
    public void atTurnStart() {
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
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(AbstractDungeon.player.hasRelic(this.relicId)){
            if(AbstractCard.CardType.ATTACK.equals(targetCard.type) && Objects.nonNull(targetCard.multiDamage) && targetCard.multiDamage.length > 0){
                this.flash();
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STR), STR));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STR + DESCRIPTIONS[1] + DESCRIPTIONS[2] + STR + DESCRIPTIONS[3];
    }
}
