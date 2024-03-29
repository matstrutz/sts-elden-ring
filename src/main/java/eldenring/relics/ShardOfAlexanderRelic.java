package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eldenring.EldenRingSTS;

public class ShardOfAlexanderRelic extends BaseRelic {
    private static final String NAME = "ShardOfAlexander";
    public static final String ID = EldenRingSTS.makeID(NAME);
    //TODO Move do special um event is done
    private static final RelicTier RARITY = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.HEAVY;
    private static final int POWER = 2;
    private boolean firstTurn = true;

    public ShardOfAlexanderRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + POWER + DESCRIPTIONS[1];
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
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, POWER), POWER));
                this.firstTurn = false;
            }
        }
    }
}
