package basicmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static basicmod.EldenRingSTS.makeID;

public class ProsthesisWearerHeirloomRelic extends BaseRelic {
    private static final String NAME = "ProsthesisWearerHeirloom";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.COMMON;
        private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int DEX = 1;
    private boolean firstTurn = true;

    public ProsthesisWearerHeirloomRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
    }

    @Override
    public void atTurnStart() {
        if (this.firstTurn) {
            this.flash();
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, DEX), DEX));
            this.firstTurn = false;
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DEX + DESCRIPTIONS[1];
    }
}
