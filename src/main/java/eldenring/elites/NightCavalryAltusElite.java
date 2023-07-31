package eldenring.elites;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;
import eldenring.monsters.BaseMonster;
import eldenring.powers.HemorrhagePower;

public class NightCavalryAltusElite extends BaseMonster {
    public final static String NAME = "Night Cavalry";
    public final static String ID = EldenRingSTS.makeID("NightCavalryAltus");

    private int turnMove = -1;
    private int stomp = 10;
    private int flailA = 10;
    private int jump = 16;
    private int flailB = 18;
    private int hemoAmout = 1;
    private int spinningSwing = 3;
    private int spinningSwingCount = 4;

    public NightCavalryAltusElite() {
        super(NAME, ID, 147, 0.0F, 0.0F, 150.0F, 286.0F, EldenRingSTS.monsterPath("NightCavalry"), 0.0F, 0.0F);
        if (AbstractDungeon.ascensionLevel >= 3) {
            stomp += 2;
            flailA += 2;
            jump += 2;
            flailB += 3;
            spinningSwing += 1;
        }

        if (AbstractDungeon.ascensionLevel >= 18) {
            stomp += 2;
            hemoAmout += 1;
            spinningSwingCount += 1;
        }

        setDmg();
    }

    private void setDmg(){
        this.damage.add(new DamageInfo(this, this.stomp));
        this.damage.add(new DamageInfo(this, this.flailA));
        this.damage.add(new DamageInfo(this, this.jump));
        this.damage.add(new DamageInfo(this, this.flailB));
        this.damage.add(new DamageInfo(this, this.spinningSwing));
    }

    @Override
    public void takeTurn() {
        switch (this.turnMove) {
            case 0 :
                flailBAction();
                calcNextMove();
                break;
            case 1:
                stompAction();
                calcNextMove();
                break;
            case 2:
                flailAAction();
                calcNextMove();
                break;
            case 3 :
                jumpAction();
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        calcNextMove();
    }

    private void calcTurn(){
        this.turnMove = (int) (Math.random() * 4);
    }

    private void flailAAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new HemorrhagePower(AbstractDungeon.player, hemoAmout)));
    }

    private void flailBAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new HemorrhagePower(AbstractDungeon.player, hemoAmout)));
    }

    private void stompAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void jumpAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void spinningSwingAction(){
        for (int i = 0; i < spinningSwingCount; i++) {
            AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new HemorrhagePower(AbstractDungeon.player, 1)));
        }
    }

    private void calcNextMove(){
        calcTurn();
        switch (this.turnMove) {
            case 0:
                this.setMove((byte)0, Intent.ATTACK, flailB);
                break;
            case 1:
                this.setMove((byte)1, Intent.ATTACK, stomp);
                break;
            case 2:
                this.setMove((byte)2, Intent.ATTACK, flailA);
                break;
            case 3:
                this.setMove((byte)3, Intent.ATTACK, jump);
                break;
            case 4:
                this.setMove((byte)3, Intent.ATTACK, spinningSwing, spinningSwingCount, true);
                break;
        }
    }
}
