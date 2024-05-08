package eldenring.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;

public class DestinedDeathPower extends BasePower {
    public static final String POWER_ID = EldenRingSTS.makeID("DestinedDeath");
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;
    private static int energyLoseCount = 1;

    public DestinedDeathPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        if(AbstractDungeon.player.equals(this.owner)){
            this.addToTop(new LoseEnergyAction(energyLoseCount));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[2];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        int endOfTurnBlock = AbstractDungeon.player.currentBlock;
        if(isPlayer && endOfTurnBlock > 0){
            this.addToBot(new LoseBlockAction(this.owner, this.owner, endOfTurnBlock));
            this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, endOfTurnBlock, DamageInfo.DamageType.HP_LOSS)));
        } else {
            energyLoseCount += 1;
        }
        this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}
