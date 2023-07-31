package eldenring.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnMyBlockBrokenPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eldenring.EldenRingSTS;

public class OmenBairnPower extends BasePower implements OnMyBlockBrokenPower {
    public static final String POWER_ID = EldenRingSTS.makeID("OmenBairn");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private boolean checkBlockEnemy = false;
    private static final int STR_GAIN = 2;

    public OmenBairnPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        if(AbstractDungeon.player.currentBlock > 0){
            this.checkBlockEnemy = true;
        }
    }
    @Override
    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target.currentBlock < 1 && this.checkBlockEnemy) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, STR_GAIN)));
            this.checkBlockEnemy = false;
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + STR_GAIN + DESCRIPTIONS[1] + DESCRIPTIONS[2];
    }

    @Override
    public void onMyBlockBroken() {
        if(this.owner.hasPower(StrengthPower.POWER_ID)){
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, StrengthPower.POWER_ID));
        }
    }
}
