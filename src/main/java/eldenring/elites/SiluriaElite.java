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

public class SiluriaElite extends BaseMonster {
    public final static String NAME = "Crucible Knight Siluria";
    public final static String FAKE_ID = "Siluria";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int turnMove = -1;
    private boolean comboStart = true;
    private int defend = 12;
    private int stomp = 10;
    private int swingA = 16;
    private int swingB = 18;
    private int stab = 19;
    private int spearSpecial = 4;
    private int spearAtkCount = 6;
    private int tailA = 18;
    private int tailB = 28;
    private int dive = 22;

    public SiluriaElite() {
        super(NAME, ID, 136, 0.0F, 0.0F, 140.0F, 267.0F, EldenRingSTS.monsterPath(FAKE_ID), 0.0F, 0.0F);
        if (AbstractDungeon.ascensionLevel >= 3) {
            stomp += 3;
            swingA += 3;
            swingB += 3;
            stab += 4;
            spearSpecial += 5;
            tailA += 5;
            tailB += 4;
            dive += 4;
        }
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
                siluriaTreeSpecial();
                calcNextMove();
                break;
            case 6:
                diveAction();
                calcNextMove();
                break;
            case 7:
                tailAAction();
                calcNextMove();
                break;
            case 8:
                tailBAction();
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        calcNextMove();
    }

    private void calcTurn(){
        if(this.currentHealth < (this.maxHealth / 2)){
            this.turnMove = ((int) (Math.random() * 5)) + 4;
            this.comboStart = false;
        }
        if (this.comboStart) {
            ++this.turnMove;
            if(this.turnMove > 4){
                this.comboStart = false;
            }
        } else {
            this.turnMove = (int) (Math.random() * 5);
        }
    }

    private void swingAAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.swingA), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void swingBAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.swingB), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void stompAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.stomp), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void defendAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.defend));
    }


    private void siluriaTreeSpecial(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        for (int i = 0; i < spearAtkCount; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.spearSpecial), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        }
    }

    private void diveAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.dive), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void tailAAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.tailA), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void tailBAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.tailB), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void stabAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.tailB), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
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
                this.setMove((byte)6, Intent.ATTACK, spearSpecial, spearAtkCount, true);
                break;
            case 6:
                this.setMove((byte)7, Intent.ATTACK, dive);
                break;
            case 7:
                this.setMove((byte)8, Intent.ATTACK, tailA);
                break;
            case 8:
                this.setMove((byte)9, Intent.ATTACK, tailB);
                break;
        }
    }
}
