package eldenring.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;

public class ExileSoldierMonster extends BaseMonster {
    public final static String NAME = "Exile Soldier";
    public final static String FAKE_ID = "ExileSoldier";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int slash = 6;
    private int block = 4;
    private int turnMove = 0;

    public ExileSoldierMonster(float offX, float offY) {
        super(NAME, ID, 19, 0.0F, 0.0F, 120.0F, 229.0F, EldenRingSTS.monsterPath("ExileSoldier"), offX, offY);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.slash += 2;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(20, 21);
            this.block += 2;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.slash += 1;
        }

        setDmg();
    }

    private void setDmg(){
        this.damage.add(new DamageInfo(this, this.slash));
    }

    @Override
    public void takeTurn() {
        switch (this.turnMove) {
            case 0 :
                stomp();
                calcNextMove();
                break;
            case 1:
                slash();
                calcNextMove();
                break;
            case 2:
                defense();
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        calcNextMove();
    }

    private void calcTurn(){
        int nextMov = (int) (Math.random() * 3);
        if(nextMov == this.turnMove) {
            calcTurn();
            return;
        }
        this.turnMove = nextMov;
    }

    private void stomp(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block));
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    private void slash(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void defense(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block));
    }

    private void calcNextMove(){
        calcTurn();
        switch (this.turnMove) {
            case 0:
                this.setMove((byte) 1, Intent.ATTACK_DEFEND, slash);
                break;
            case 1:
                this.setMove((byte) 2, Intent.ATTACK, slash);
                break;
            case 2:
                this.setMove((byte) 3, Intent.DEFEND, block);
                break;
        }
    }
}
