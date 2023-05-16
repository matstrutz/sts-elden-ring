package eldenring.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import eldenring.EldenRingSTS;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GiantLandOctopusMonster extends BaseMonster {
    public final static String NAME = "Giant Land Octupus";
    public final static String FAKE_ID = "GiantLandOctupus";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int bounce = 8;
    private int tentacle = 2;
    private int tentacleCount = 5;
    private int slash = 4;
    private List<Integer> turnPattern = Arrays.asList(0,1,2);
    private int countTurnPattern = 0;
    private int healCount = 0;

    public GiantLandOctopusMonster(float offX, float offY) {
        super(NAME, ID, 56, 0.0F, 0.0F, 500.0F, 282.0F, EldenRingSTS.monsterPath(FAKE_ID), offX, offY);
        Collections.shuffle(turnPattern);
        if (AbstractDungeon.ascensionLevel >= 2) {
            bounce += 2;
            tentacle += 1;
            tentacleCount += 1;
            slash += 1;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            bounce += 3;
            tentacleCount += 2;
            slash += 1;
        }

        setDmg();
    }

    private void setDmg(){
        this.damage.add(new DamageInfo(this, this.bounce));
        this.damage.add(new DamageInfo(this, this.tentacle));
        this.damage.add(new DamageInfo(this, this.slash));
    }

    @Override
    public void takeTurn() {
        if((this.currentHealth < (this.maxHealth/2) && healCount == 1)){
            firstHeal();
            calcNextMove();
            return;
        }
        if((this.currentHealth < (this.maxHealth/2) && healCount == 2)){
            secondHeal();
            healCount += 1;
            calcNextMove();
            return;
        }
        switch (turnPattern.get(countTurnPattern)) {
            case 0 :
                patternA();
                countTurnPattern = countTurnPattern + 1;
                calcNextMove();
                break;
            case 1:
                patternB();
                countTurnPattern = countTurnPattern + 1;
                calcNextMove();
                break;
            case 2:
                patternC();
                countTurnPattern = countTurnPattern + 1;
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        switch (turnPattern.get(countTurnPattern)) {
            case 0:
                this.setMove((byte)1, Intent.ATTACK, bounce, 1, false);
                break;
            case 1:
                this.setMove((byte)2, Intent.ATTACK, tentacle, tentacleCount, true);
                break;
            case 2:
                this.setMove((byte)3, Intent.ATTACK, slash);
                break;
        }
    }

    private void patternA(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    private void patternB(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        for (int i = 0; i < tentacleCount; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    private void patternC(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void firstHeal(){
        AbstractDungeon.actionManager.addToBottom(new AnimateShakeAction(this, 0.3F,0.5F));
        AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth/5, 0.0F));
    }

    private void secondHeal(){
        AbstractDungeon.actionManager.addToBottom(new AnimateShakeAction(this, 0.3F,0.5F));
        AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth/5, 0.0F));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new WeakPower(this, 2, true), 2));
    }

    private void calcNextMove(){
        if((this.currentHealth < (this.maxHealth/2) && healCount == 0)){
            this.setMove((byte)4, Intent.UNKNOWN);
            healCount += 1;
            return;
        }
        if((this.currentHealth < (this.maxHealth/2) && healCount == 1)){
            this.setMove((byte)5, Intent.UNKNOWN);
            healCount += 1;
            return;
        }
        if(countTurnPattern > (turnPattern.size() - 1)){
            Collections.shuffle(turnPattern);
            countTurnPattern = 0;
        }
        switch (turnPattern.get(countTurnPattern)) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)1, Intent.ATTACK, bounce, 1, false));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)2, Intent.ATTACK, tentacle, tentacleCount, false));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)3, Intent.ATTACK, slash, 1, false));
                break;
        }
    }
}
