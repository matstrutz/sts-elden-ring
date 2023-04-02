package basicmod.relics;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Objects;

import static basicmod.EldenRingSTS.makeID;

public class WingedSwordInsigniaRelic extends BaseRelic {
    private static final String NAME = "WingedSwordInsignia";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int EXTRA_ATTACK = 2;

    public WingedSwordInsigniaRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(AbstractDungeon.player.hasRelic(this.relicId)){
            this.flash();
            if(AbstractCard.CardType.ATTACK.equals(targetCard.type) && Objects.nonNull(targetCard.multiDamage) && targetCard.multiDamage.length > 0){
                int calcExtraDmg = 0;
                for (int i = 0; i < targetCard.multiDamage.length; i++) {
                    targetCard.multiDamage[i] += calcExtraDmg;
                    calcExtraDmg += 2;
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + EXTRA_ATTACK + DESCRIPTIONS[1];
    }
}
