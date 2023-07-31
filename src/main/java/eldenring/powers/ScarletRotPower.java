package eldenring.powers;

import basemod.interfaces.MaxHPChangeSubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnMyBlockBrokenPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eldenring.EldenRingSTS;

public class ScarletRotPower extends BasePower implements MaxHPChangeSubscriber {
    public static final String POWER_ID = EldenRingSTS.makeID("ScarletRot");
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    public ScarletRotPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        if((this.owner.maxHealth / 10) < this.amount){
            this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, (this.owner.maxHealth / 10) + 2, DamageInfo.DamageType.HP_LOSS)));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + ((this.owner.maxHealth / 10) - 1)  + DESCRIPTIONS[1] + ((this.owner.maxHealth / 10) + 2) + DESCRIPTIONS[2];
    }

    @Override
    public int receiveMaxHPChange(int amount) {
        this.description = DESCRIPTIONS[0] + ((this.owner.maxHealth / 10) - 1)  + DESCRIPTIONS[1] + ((this.owner.maxHealth / 10) + 2) + DESCRIPTIONS[2];
        return amount;
    }
}
