package eldenring.elites;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;
import eldenring.monsters.BaseMonster;
import eldenring.powers.ScarletRotPower;
import eldenring.vfx.GlintstonePebbleVFX;
import eldenring.vfx.SacredPhalanxVFX;

public class FinlayElite extends BaseMonster {
    public final static String NAME = "Cleanrot Knight Finlay";
    public final static String FAKE_ID = "Finlay";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int turnMove = -1;
    private int slashA = 10;
    private int spearGuard = 10;
    private int swordJab = 11;
    private int slashB = 12;
    private int spearStab = 5;
    private int spearStabCount = 2;
    private int impale = 5;
    private int impaleRotAmount = 5;
    private int sacredPhalanx = 18;
    private int retreatPhalanx = 10;
    private int rushdownRotAmount = 3;

    public FinlayElite() {
        super(NAME, ID, 145, 0.0F, 0.0F, 150.0F, 286.0F, EldenRingSTS.monsterPath("CleanrotKnight"), 0.0F, 0.0F);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 3) {
            slashA += 3;
            spearGuard += 2;
            swordJab += 2;
            slashB += 3;
            spearStab += 3;
            impale += 1;;
        }

        if(AbstractDungeon.ascensionLevel >= 8){
            this.setHp(151, 155);
        }

        if (AbstractDungeon.ascensionLevel >= 18) {
            slashA += 2;
            spearGuard += 2;
            spearStabCount += 1;
            impaleRotAmount += 2;
            retreatPhalanx += 3;
            sacredPhalanx += 3;
        }

        setDmg();
    }

    private void setDmg(){
        this.damage.add(new DamageInfo(this, this.slashA));
        this.damage.add(new DamageInfo(this, this.spearGuard));
        this.damage.add(new DamageInfo(this, this.swordJab));
        this.damage.add(new DamageInfo(this, this.slashB));
        this.damage.add(new DamageInfo(this, this.spearStab));
        this.damage.add(new DamageInfo(this, this.impale));
        this.damage.add(new DamageInfo(this, this.sacredPhalanx));
        this.damage.add(new DamageInfo(this, this.retreatPhalanx));
    }

    @Override
    public void takeTurn() {
        switch (this.turnMove) {
            case 0 :
                spearStabAction();
                calcNextMove();
                break;
            case 1:
                slashAAction();
                calcNextMove();
                break;
            case 2:
                spearGuardAction();
                calcNextMove();
                break;
            case 3 :
                swordJabAction();
                calcNextMove();
                break;
            case 4:
                slashBAction();
                calcNextMove();
                break;
            case 5:
                impaleAction();
                calcNextMove();
                break;
            case 6:
                sacredPhalanxAction();
                calcNextMove();
                break;
            case 7:
                retreatPhalanxAction();
                calcNextMove();
                break;
            case 8:
                rushdownAction();
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        calcNextMove();
    }

    private void calcTurn(){
        this.turnMove = (int) (Math.random() * 9);
    }

    private void spearGuardAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.spearGuard / 2));
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ScarletRotPower(AbstractDungeon.player, 1)));
    }

    private void swordJabAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    private void slashAAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    private void spearStabAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        for (int i = 0; i < spearStabCount; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ScarletRotPower(AbstractDungeon.player, 1)));
        }
    }

    private void slashBAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void impaleAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(5), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ScarletRotPower(AbstractDungeon.player, impaleRotAmount)));
    }


    private void sacredPhalanxAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new SacredPhalanxVFX().animation(AbstractDungeon.player), SacredPhalanxVFX.DURAT));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(6)));
    }

    //TODO SET CUSTOM ANIMATION
    private void retreatPhalanxAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.retreatPhalanx));
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(7), AbstractGameAction.AttackEffect.LIGHTNING));
    }

    private void rushdownAction(){
        AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
        if(AbstractDungeon.player.hasPower(ScarletRotPower.POWER_ID)){
            this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this, AbstractDungeon.player.getPower(ScarletRotPower.POWER_ID).amount * 2, DamageInfo.DamageType.HP_LOSS)));
            this.addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.getPower(ScarletRotPower.POWER_ID)));
        } else {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ScarletRotPower(AbstractDungeon.player, rushdownRotAmount)));
        }
    }

    private void calcNextMove(){
        calcTurn();
        switch (this.turnMove) {
            case 0:
                this.setMove((byte)1, Intent.ATTACK_DEBUFF, spearStab, spearStabCount, true);
                break;
            case 1:
                this.setMove((byte)2, Intent.ATTACK, slashA);
                break;
            case 2:
                this.setMove((byte)3, Intent.ATTACK_DEFEND, spearGuard);
                break;
            case 3:
                this.setMove((byte)4, Intent.ATTACK, swordJab);
                break;
            case 4:
                this.setMove((byte)5, Intent.ATTACK, slashB);
                break;
            case 5:
                this.setMove((byte)6, Intent.ATTACK_DEBUFF, impale);
                break;
            case 6:
                this.setMove((byte)7, Intent.ATTACK, sacredPhalanx);
                break;
            case 7:
                this.setMove((byte)8, Intent.ATTACK, retreatPhalanx);
                break;
            case 8:
                this.setMove((byte)9, Intent.UNKNOWN);
                break;
        }
    }
}
