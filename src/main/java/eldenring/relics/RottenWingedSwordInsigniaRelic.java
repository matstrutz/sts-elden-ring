package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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

    public RottenWingedSwordInsigniaRelic() {
        super(ID, NAME, RARITY, SOUND);
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
        return DESCRIPTIONS[0] + STR + DESCRIPTIONS[1];
    }
}
