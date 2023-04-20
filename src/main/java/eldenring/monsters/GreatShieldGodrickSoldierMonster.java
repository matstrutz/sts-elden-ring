package eldenring.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;

public class GreatShieldGodrickSoldierMonster extends BaseMonster {
    public final static String NAME = "Godrick Soldier Greatshield";
    public final static String FAKE_ID = "GodrickSoldierGreatshield";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int stock = 7;
    private int greatShieldBlock = 14;
    private int turnMove = 0;
    private int impale = 16;
    private int turnCount = 0;

    public GreatShieldGodrickSoldierMonster(float offX, float offY) {
        super(NAME, ID, 29, 0.0F, 0.0F, 130.0F, 248.0F, EldenRingSTS.monsterPath("GodrickSoldier"), offX, offY);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.stock += 2;
            this.impale += 3;
        }
    }

    @Override
    public void takeTurn() {
        switch (this.turnMove) {
            case 0 :
                stoc();
                calcNextMove();
                break;
            case 1:
                stocShield();
                calcNextMove();
                break;
            case 2:
                defense();
                calcNextMove();
                break;
            case 3:
                impale();
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        calcNextMove();
    }

    private void calcTurn(){
        int nextMov = (int) (Math.random() * 2);
        if(nextMov == this.turnMove) {
            calcTurn();
        }
        this.turnMove = nextMov;
    }

    private void stoc(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.stock), AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    private void stocShield(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, stock));
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.stock), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    private void defense(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, greatShieldBlock));
    }

    private void impale(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.impale), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    private void calcNextMove(){
        calcTurn();
        turnCount++;
        if(turnCount > 4){
            this.turnMove = 3;
            turnCount = 0;
        }
        switch (this.turnMove) {
            case 0:
                this.setMove((byte) 1, Intent.ATTACK, stock);
                break;
            case 1:
                this.setMove((byte) 2, Intent.ATTACK_DEFEND, stock);
                break;
            case 2:
                this.setMove((byte) 3, Intent.DEFEND, greatShieldBlock);
                break;
            case 3:
                this.setMove((byte) 4, Intent.ATTACK, impale);
                break;
        }
    }
}
