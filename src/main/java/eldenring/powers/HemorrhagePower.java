package eldenring.powers;

import basemod.interfaces.MaxHPChangeSubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eldenring.EldenRingSTS;

public class HemorrhagePower extends BasePower implements OnReceivePowerPower, MaxHPChangeSubscriber {
    public static final String POWER_ID = EldenRingSTS.makeID("HemorrhagePower");
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    public HemorrhagePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + ((this.owner.maxHealth / 10) - 1)  + DESCRIPTIONS[1] + ((this.owner.maxHealth / 7) + 3) + DESCRIPTIONS[2];
    }

    @Override
    public int receiveMaxHPChange(int amount) {
        this.description = DESCRIPTIONS[0] + ((this.owner.maxHealth / 10) - 1)  + DESCRIPTIONS[1] + ((this.owner.maxHealth / 7) + 3) + DESCRIPTIONS[2];
        return amount;
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1, int stackAmount) {
        final int calcAmount = stackAmount + this.amount;
        if(this.ID.equals(abstractPower.ID)){
            if((this.owner.maxHealth / 10) < calcAmount){
                this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, (this.owner.maxHealth / 7) + 3, DamageInfo.DamageType.NORMAL)));
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }
        return stackAmount;
    }
}
