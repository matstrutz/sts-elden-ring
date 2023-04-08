package basicmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Objects;

import static basicmod.EldenRingSTS.makeID;

public class WingedSwordInsigniaRelic extends BaseRelic {
    private static final String NAME = "WingedSwordInsignia";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;
    private static final int STR = 1;

    public WingedSwordInsigniaRelic() {
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
