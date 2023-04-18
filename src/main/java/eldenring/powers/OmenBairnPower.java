package eldenring.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eldenring.EldenRingSTS;

public class OmenBairnPower extends BasePower {
    public static final String POWER_ID = EldenRingSTS.makeID("OmenBairn");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private boolean checkBlockEnemy = false;
    private boolean checkBlockSelf = false;
    private static final int STR_GAIN = 2;
    public OmenBairnPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(target.currentBlock > 0){
            this.checkBlockEnemy = true;
        }
    }
    @Override
    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target.currentBlock < 1 && this.checkBlockEnemy) {
            this.owner.addPower(new StrengthPower(this.owner, STR_GAIN));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(this.owner.currentBlock > 0){
            this.checkBlockSelf = true;
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if(this.owner.currentBlock < 1 && checkBlockSelf){
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new LoseStrengthPower(this.owner, this.owner.getPower(StrengthPower.POWER_ID).amount)));
            this.owner.addPower(new StrengthPower(this.owner, STR_GAIN));
        }
        return damage;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + STR_GAIN + DESCRIPTIONS[1] + DESCRIPTIONS[2];
    }
}
