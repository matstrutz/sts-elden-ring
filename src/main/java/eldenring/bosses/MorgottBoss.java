package eldenring.bosses;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eldenring.EldenRingSTS;
import eldenring.monsters.BaseMonster;
import eldenring.powers.OmenBairnPower;

public class MorgottBoss extends BaseMonster {
    public final static String NAME = "Morgott, The Omen King";
    public final static String FAKE_ID = "Morgott";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int holyBladeRainDmg = 4;
    private int holyBladeRainCount = 6;
    private int tailSwipeDmg = 12;
    private int tailSwipeDef = 15;
    private int holySpearThrowDmg = 16;
    private int holyDaggerDmg = 4;
    private int holyDaggerCount = 5;
    private int holyHammerDmg = 20;
    private int holySwordDmg = 10;
    private int holySpearDmg = 10;
    private int holySpearCount = 10;
    private int defPreCurse = 40;
    private int cursedBloodSliceDmg = 60;
    private boolean curseTrigger = true;
    private boolean curseFirstMove = true;
    private int turnMove = 0;
    private boolean encounterStart = true;
    public MorgottBoss() {
        super(NAME, ID, 303, 0.0F, 0.0F, 550.0F, 486.0F, EldenRingSTS.monsterPath("Morgott_001"), 5.0F, 0.0F);
        if (AbstractDungeon.ascensionLevel >= 4) {
            holyBladeRainDmg += 5;
            tailSwipeDmg += 5;
            holySpearThrowDmg += 5;
            holyDaggerDmg += 5;
            holyHammerDmg += 5;
            holySpearDmg += 5;
            cursedBloodSliceDmg += 10;
        }

        if (AbstractDungeon.ascensionLevel >= 9) {
            defPreCurse += 10;
            tailSwipeDef += 5;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            holyBladeRainDmg += 3;
            tailSwipeDmg += 3;
            holySpearThrowDmg += 3;
            holyDaggerDmg += 3;
            holyHammerDmg += 3;
            holySpearDmg += 3;
            cursedBloodSliceDmg += 15;
        }

        setDmg();
    }

    private void setDmg(){
        this.damage.add(new DamageInfo(this, this.holyBladeRainDmg));
        this.damage.add(new DamageInfo(this, this.tailSwipeDmg));
        this.damage.add(new DamageInfo(this, this.holySpearThrowDmg));
        this.damage.add(new DamageInfo(this, this.holyDaggerDmg));
        this.damage.add(new DamageInfo(this, this.holyHammerDmg));
        this.damage.add(new DamageInfo(this, this.holySpearDmg));
        this.damage.add(new DamageInfo(this, this.holySwordDmg));
        this.damage.add(new DamageInfo(this, this.cursedBloodSliceDmg));
    }


    @Override
    public void takeTurn() {
        switch (this.turnMove) {
            case 0 :
                holyBladeRain();
                calcNextMove();
                break;
            case 1:
                tailSwipe();
                calcNextMove();
                break;
            case 2:
                holySpearThrow();
                calcNextMove();
                break;
            case 3 :
                holyDagger();
                calcNextMove();
                break;
            case 4:
                holyHammer();
                calcNextMove();
                break;
            case 5:
                holySword();
                calcNextMove();
                break;
            case 6:
                holySpear();
                calcNextMove();
                break;
            case 7:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH"));
                cursedBloodSlice();
                calcNextMove();
                break;
            case 8:
                preCurseBlock();
                calcNextMove();
                break;
            case 10:
                this.addToBot(new ApplyPowerAction(this, this, new OmenBairnPower(this, 1)));
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if(this.encounterStart){
            this.encounterStart = false;
            this.setMove((byte)10, Intent.BUFF);
            this.turnMove = 10;
            return;
        }
        calcNextMove();
    }

    private void calcTurn(){
        int nextMov = (int) (Math.random() * 7);
        if(nextMov == this.turnMove) {
            calcTurn();
            return;
        }
        this.turnMove = nextMov;
    }
    private void holyBladeRain(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        for (int i = 0; i < holyBladeRainCount - 1; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }

    private void tailSwipe(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, tailSwipeDef));
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    private void holySpearThrow(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    private void holyDagger(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        for (int i = 0; i < holyDaggerCount - 1; i++) {
            if((i % 2) == 0){
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
            } else {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            }
        }
    }

    private void holyHammer(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void holySword(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(6), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, holySwordDmg));
    }

    private void holySpear(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        for (int i = 0; i < holySpearCount - 1; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(5), AbstractGameAction.AttackEffect.LIGHTNING));
        }
    }

    private void preCurseBlock(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, defPreCurse));
        this.addToBot(new ApplyPowerAction(this, this, new DexterityPower(this, 3), 3));
    }

    private void cursedBloodSlice(){
        AbstractDungeon.actionManager.addToBottom(new AnimateShakeAction(this, 1F,1F));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(7), AbstractGameAction.AttackEffect.LIGHTNING));
    }

    private void calcNextMove(){
        calcTurn();
        if((this.currentHealth < (this.maxHealth/3)) && this.curseTrigger){
            if(this.curseFirstMove){
                this.curseFirstMove = false;
                this.setMove((byte)8, Intent.DEFEND_BUFF, defPreCurse);
                this.turnMove = 8;
            } else {
                this.curseTrigger = false;
                this.setMove((byte)9, Intent.ATTACK, cursedBloodSliceDmg/6, 6, true);
                this.turnMove = 7;
                this.holySwordDmg += 10;
            }
        }
        switch (this.turnMove) {
            case 0:
                this.setMove((byte)1, Intent.ATTACK, holyBladeRainDmg, holyBladeRainCount, true);
                break;
            case 1:
                this.setMove((byte)2, Intent.ATTACK_DEFEND, tailSwipeDmg);
                break;
            case 2:
                this.setMove((byte)3, Intent.ATTACK, holySpearThrowDmg);
                break;
            case 3:
                this.setMove((byte)4, Intent.ATTACK, holyDaggerDmg, holyDaggerCount, true);
                break;
            case 4:
                this.setMove((byte)5, Intent.ATTACK, holyHammerDmg);
                break;
            case 5:
                this.setMove((byte)6, Intent.ATTACK_DEFEND, holySwordDmg);
                break;
            case 6:
                this.setMove((byte)7, Intent.ATTACK, holySpearDmg, holySpearCount,true);
                break;
        }
    }
}
