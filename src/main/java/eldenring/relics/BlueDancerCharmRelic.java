package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eldenring.EldenRingSTS;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlueDancerCharmRelic extends BaseRelic {
    private static final String NAME = "BlueDancerCharm";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int ENERGY = 1;
    private static final int STR = 1;
    private static final int DEX = 1;

    private List<Integer> buff = Arrays.asList(0,1,2);
    public BlueDancerCharmRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atTurnStart() {
        if(!AbstractDungeon.player.hasAnyPotions()){
            this.flash();
            Collections.shuffle(buff);

            switch (buff.get(0)) {
                case 0 :
                    this.addToTop(new GainEnergyAction(ENERGY));
                    break;
                case 1:
                    this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, DEX), DEX));
                    break;
                case 2:
                    this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STR), STR));
                    break;
            }
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STR + DESCRIPTIONS[1] + DESCRIPTIONS[2] + DEX + DESCRIPTIONS[3] + DESCRIPTIONS[4];
    }
}
