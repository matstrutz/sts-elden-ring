package eldenring.elites;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;
import eldenring.monsters.BaseMonster;

public class OlegElite extends BaseMonster {
    public final static String NAME = "Banished Knight Oleg";
    public final static String FAKE_ID = "Oleg";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int turnMove = -1;
    private int defend = 12;
    private int defendB = 16;
    private int stomp = 7;
    private int swingA = 10;
    private int swingB = 11;
    private int stab = 11;
    private int fireBreath = 16;

    public OlegElite() {
        super(NAME, ID, 72, 0.0F, 0.0F, 150.0F, 286.0F, EldenRingSTS.monsterPath("BanishedKnight"), 0.0F, 0.0F);
        if (AbstractDungeon.ascensionLevel >= 3) {
            stomp += 2;
            swingA += 2;
            swingB += 2;
            stab += 3;
            fireBreath += 4;
        }


        if (AbstractDungeon.ascensionLevel >= 7) {
            defend += 6;
            defendB += 4;
        }

        if (AbstractDungeon.ascensionLevel >= 18) {
            stomp += 2;
            fireBreath += 2;
        }

        setDmg();
    }

    private void setDmg(){
        this.damage.add(new DamageInfo(this, this.stomp));
        this.damage.add(new DamageInfo(this, this.swingA));
        this.damage.add(new DamageInfo(this, this.swingB));
        this.damage.add(new DamageInfo(this, this.stab));
        this.damage.add(new DamageInfo(this, this.fireBreath));
    }

    @Override
    public void takeTurn() {
        switch (this.turnMove) {
            case 0 :
                defendAction();
                calcNextMove();
                break;
            case 1:
                stompAction();
                calcNextMove();
                break;
            case 2:
                swingAAction();
                calcNextMove();
                break;
            case 3 :
                swingBAction();
                calcNextMove();
                break;
            case 4:
                stabAction();
                calcNextMove();
                break;
            case 5:
                fireBreathAction();
                calcNextMove();
                break;
            case 6:
                defendBAction();
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        calcNextMove();
    }

    private void calcTurn(){
        this.turnMove = (int) (Math.random() * 7);
    }

    private void swingAAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void swingBAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void stompAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void defendAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.defend));
    }

    private void defendBAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.defend));
    }

    private void fireBreathAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void stabAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void calcNextMove(){
        calcTurn();
        switch (this.turnMove) {
            case 0:
                this.setMove((byte)1, Intent.DEFEND, defend);
                break;
            case 1:
                this.setMove((byte)2, Intent.ATTACK, stomp);
                break;
            case 2:
                this.setMove((byte)3, Intent.ATTACK, swingA);
                break;
            case 3:
                this.setMove((byte)4, Intent.ATTACK, swingB);
                break;
            case 4:
                this.setMove((byte)5, Intent.ATTACK, stab);
                break;
            case 5:
                this.setMove((byte)6, Intent.ATTACK, fireBreath);
                break;
            case 6:
                this.setMove((byte)10, Intent.DEFEND, defendB);
                break;
        }
    }
}
