package eldenring.powers;

import basemod.interfaces.OnPowersModifiedSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eldenring.EldenRingSTS;

public class HemorrhagePower extends BasePower implements PostPowerApplySubscriber {
    public static final String POWER_ID = EldenRingSTS.makeID("ScarletRot");
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    public HemorrhagePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + ((this.owner.maxHealth / 10) - 1)  + DESCRIPTIONS[1] + ((this.owner.maxHealth / 10) + 2) + DESCRIPTIONS[2];
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if(POWER_ID.equals(abstractPower.ID)){
            if((this.owner.maxHealth / 10) < this.amount){
                this.addToBot(new DamageAction(this.owner, new DamageInfo(AbstractDungeon.player, (this.owner.maxHealth / 6) + 3, DamageInfo.DamageType.NORMAL)));
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }
    }
}
