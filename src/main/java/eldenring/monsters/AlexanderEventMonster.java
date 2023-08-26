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

public class AlexanderEventMonster extends BaseMonster {
    public final static String NAME = "Alexander";
    public final static String FAKE_ID = "AlexanderEvent";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int slash = 5;
    private int block = 10;
    private final int qtdAttackMax = 4;
    private int qtdAttackTurn = 0;
    private int turnMove = 0;

    public AlexanderEventMonster() {
        super(NAME, ID, 111, 0.0F, 0.0F, 450.0F, 339.0F, EldenRingSTS.monsterPath("Alexander"), 0.0F, 0.0F);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.slash += 1;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
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
            case 0:
                slash();
                calcNextMove();
                break;
            case 1:
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
        this.qtdAttackTurn = (int) ((Math.random() * qtdAttackMax) + 1);
        int nextMov = (int) (Math.random() * 2);
        if(nextMov == this.turnMove) {
            calcTurn();
            return;
        }
        this.turnMove = nextMov;
    }

    private void slash(){
        for (int i = 0; i < qtdAttackTurn; i++) {
            AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    private void defense(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block + qtdAttackTurn));
    }

    private void calcNextMove(){
        calcTurn();
        switch (this.turnMove) {
            case 0:
                this.setMove((byte) 1, Intent.ATTACK, slash, qtdAttackTurn, true);
                break;
            case 1:
                this.setMove((byte) 2, Intent.DEFEND, block + qtdAttackTurn);
                break;
        }
    }
}
