package eldenring.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;

public class TorchGodrickSoldierMonster extends BaseMonster {
    public final static String NAME = "Godrick Soldier Torch";
    public final static String FAKE_ID = "GodrickSoldierTorch";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int torchAttack = 4;
    private int slash = 9;
    private int block = 4;
    private int turnMove = 0;

    public TorchGodrickSoldierMonster(float offX, float offY) {
        super(NAME, ID, 26, 0.0F, 0.0F, 130.0F, 248.0F, EldenRingSTS.monsterPath("GodrickSoldier"), offX, offY);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.torchAttack += 2;
            this.slash += 2;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.block += 2;
            this.setHp(27, 29);
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.torchAttack += 1;
            this.slash += 2;
        }

        setDmg();
    }

    private void setDmg(){
        this.damage.add(new DamageInfo(this, this.slash));
        this.damage.add(new DamageInfo(this, this.torchAttack));
    }

    @Override
    public void takeTurn() {
        switch (this.turnMove) {
            case 0 :
                burn();
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

    private void burn(){
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        Burn b = new Burn();

        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(b, 1));
    }

    private void stocShield(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void defense(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block));    }

    private void calcNextMove(){
        calcTurn();
        switch (this.turnMove) {
            case 0:
                this.setMove((byte) 1, Intent.ATTACK_DEBUFF, torchAttack);
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
