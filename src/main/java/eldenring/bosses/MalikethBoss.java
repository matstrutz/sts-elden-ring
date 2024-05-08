package eldenring.bosses;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eldenring.EldenRingSTS;
import eldenring.monsters.BaseMonster;
import eldenring.powers.DestinedDeathPower;

public class MalikethBoss extends BaseMonster {
    public final static String NAME = "Maliketh, The Black Blade";
    public final static String FIRST_PHASE_NAME = "Beast Clergyman";
    public final static String FAKE_ID = "Maliketh";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);
    public final static int SECOND_PHASE_PERCENT = 65;
    public Boolean isMaliketh = Boolean.FALSE;

    private int turnMove = 0;
    private int swingDmg = 12;
    private int swingDef = 15;
    private int flipDmg = 13;
    private int flipDef = 18;
    private int slingDmg = 13;
    private int quickSlingDmg = 5;
    private int quickSlingCount = 3;
    private int blackBladeDmg = 16;
    private int quickBlackBladeDmg = 4;
    private int quickBlackBladeCount = 5;
    private int lungeDmg = 18;
    private int lungeUpDmg = 16;
    private int destinedDeathDmg = 19;
    private int destinedDeathUpDmg = 17;
    private int pileDriverDmg = 25;
    private int roarDef = 35;
    private int fixedDef = 10;

    public MalikethBoss() {
        super(FIRST_PHASE_NAME, ID, 285, 0.0F, 0.0F, 484.0F, 516.0F, EldenRingSTS.monsterPath("BeastClergyman"), 5.0F, 0.0F);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 4) {
            slingDmg += 1;
            blackBladeDmg += 2;
            quickBlackBladeDmg += 1;
            lungeDmg += 2;
            lungeUpDmg += 2;
            destinedDeathDmg += 4;
            destinedDeathUpDmg += 3;
        }

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(288, 298);
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            pileDriverDmg += 5;
        }

        setDmg();
    }

    private void setDmg(){
        //FIRST PHASE DMG 0 - 4
        this.damage.add(new DamageInfo(this, this.swingDmg));
        this.damage.add(new DamageInfo(this, this.slingDmg));
        this.damage.add(new DamageInfo(this, this.quickSlingDmg));
        this.damage.add(new DamageInfo(this, this.lungeDmg));
        this.damage.add(new DamageInfo(this, this.lungeUpDmg));

        //SECOND PHASE DMG 5 - 10
        this.damage.add(new DamageInfo(this, this.flipDmg));
        this.damage.add(new DamageInfo(this, this.blackBladeDmg));
        this.damage.add(new DamageInfo(this, this.quickBlackBladeDmg));
        this.damage.add(new DamageInfo(this, this.destinedDeathDmg));
        this.damage.add(new DamageInfo(this, this.destinedDeathUpDmg));
        this.damage.add(new DamageInfo(this, this.pileDriverDmg));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly("BEAST_CLERGYMAN", true);
    }

    @Override
    public void takeTurn() {
        switch (this.turnMove) {
            case 0:
                swingOrFlip();
                calcNextMove();
                break;
            case 1:
                slingOrBlackBlade();
                calcNextMove();
                break;
            case 2:
                slingQuickOrBlackBladeQuick();
                calcNextMove();
                break;
            case 3:
                lungeOrDestinedDeath();
                calcNextMove();
                break;
            case 4:
                lungeUpOrDestinedDeathUp();
                calcNextMove();
                break;
            case 5:
                roar();
                calcNextMove();
                break;
            case 6:
                pileDriver();
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        calcNextMove();
    }

    private void calcTurn(){
        int nextMov = (int) (Math.random() * (isMaliketh ? 7 : 5));
        if(nextMov == this.turnMove) {
            calcTurn();
            return;
        }
        this.turnMove = nextMov;
    }

    private void swingOrFlip() {
        if (isMaliketh) {
            AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, flipDef));
            AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(5), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        } else {
            AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, swingDef));
            AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    private void slingOrBlackBlade(){
        if(isMaliketh){
            AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(6), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        } else {
            AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    private void slingQuickOrBlackBladeQuick(){
        if(isMaliketh){
            AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
            for (int i = 0; i < quickBlackBladeCount; i++) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(7), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        } else {
            AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
            for (int i = 0; i < quickSlingCount; i++) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
    }

    private void lungeOrDestinedDeath(){
        if(isMaliketh){
            AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(8), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DestinedDeathPower(AbstractDungeon.player, 1)));
        } else {
            AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }

    private void lungeUpOrDestinedDeathUp(){
        boolean playerHasBlock = AbstractDungeon.player.currentBlock > 0;
        if(isMaliketh){
            AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(9), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DestinedDeathPower(AbstractDungeon.player, 1)));
            if(playerHasBlock){
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, fixedDef));
            }
        } else {
            AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            if(playerHasBlock){
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, fixedDef));
            }
        }
    }

    private void roar(){
        AbstractDungeon.actionManager.addToBottom(new AnimateShakeAction(this, 0.5F, 0.5F));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, roarDef));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DestinedDeathPower(AbstractDungeon.player, 1)));
    }

    private void pileDriver(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(10), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void calcNextMove() {
        calcTurn();
        if(!isMaliketh){
            if(((double) this.currentHealth / this.maxHealth * 100 ) < SECOND_PHASE_PERCENT){
                changePhase();

                int healQtd = (this.maxHealth / 100 * SECOND_PHASE_PERCENT) - this.currentHealth;
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, healQtd));

                this.turnMove = 5;
            }
        }
        switch (this.turnMove) {
            case 0:
                this.setMove((byte) 1, Intent.ATTACK_DEFEND, isMaliketh ? flipDmg : swingDmg);
                break;
            case 1:
                this.setMove((byte) 2, Intent.ATTACK, isMaliketh ? blackBladeDmg : slingDmg);
                break;
            case 2:
                this.setMove((byte) 3, Intent.ATTACK, isMaliketh ? quickBlackBladeDmg : quickSlingDmg, isMaliketh ? quickBlackBladeCount : quickSlingCount, true);
                break;
            case 3:
                this.setMove((byte) 4, Intent.ATTACK, isMaliketh ? destinedDeathDmg : lungeDmg);
                break;
            case 4:
                this.setMove((byte) 5, Intent.ATTACK, isMaliketh ? destinedDeathUpDmg : lungeUpDmg);
                break;
            case 5:
                this.setMove((byte) 6, Intent.UNKNOWN);
                break;
            case 6:
                this.setMove((byte) 7, Intent.ATTACK, pileDriverDmg);
                break;
        }
    }

    private void changePhase(){
        AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.75F));
        this.img = ImageMaster.loadImage(EldenRingSTS.monsterPath("Maliketh"));
        this.name = NAME;
        this.isMaliketh = true;
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly("MALIKETH", true);

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(308, 318);
        } else {
            this.setHp(299, 305);
        }
    }
}
