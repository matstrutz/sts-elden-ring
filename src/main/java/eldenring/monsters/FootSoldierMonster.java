package eldenring.monsters;

import eldenring.EldenRingSTS;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FootSoldierMonster extends BaseMonster {
    public final static String NAME = "Foot Soldier";
    public final static String FAKE_ID = "FootSoldier";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int stab = 8;
    private int stabB = 5;
    private int def = 6;
    private List<Integer> turnPattern = Arrays.asList(0,1,2);
    private int countTurnPattern = 0;

    public FootSoldierMonster(float offX, float offY) {
        super(NAME, ID, 20, 0.0F, 0.0F, 150.0F, 250.0F, EldenRingSTS.monsterPath(FAKE_ID), offX, offY);
        Collections.shuffle(turnPattern);
        if (AbstractDungeon.ascensionLevel >= 2) {
            stab += 2;
            stabB += 2;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(22, 23);
            def += 2;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            stab += 2;
            stabB += 2;
        }

        setDmg();
    }

    private void setDmg(){
        this.damage.add(new DamageInfo(this, this.stab));
        this.damage.add(new DamageInfo(this, this.stabB));
    }

    @Override
    public void takeTurn() {
        switch (turnPattern.get(countTurnPattern)) {
            case 0 :
                patternA();
                countTurnPattern = countTurnPattern + 1;
                calcNextMove();
                break;
            case 1:
                patternB();
                countTurnPattern = countTurnPattern + 1;
                calcNextMove();
                break;
            case 2:
                patternC();
                countTurnPattern = countTurnPattern + 1;
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        switch (turnPattern.get(countTurnPattern)) {
            case 0:
                this.setMove((byte)1, Intent.ATTACK, stab, 1, false);
                break;
            case 1:
                this.setMove((byte)2, Intent.ATTACK, stabB, 1, false);
                break;
            case 2:
                this.setMove((byte)3, Intent.DEFEND, def);
                break;
        }
    }

    private void patternA(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    private void patternB(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void patternC(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, def));
    }

    private void calcNextMove(){
        if(countTurnPattern > (turnPattern.size() - 1)){
            Collections.shuffle(turnPattern);
            countTurnPattern = 0;
        }
        switch (turnPattern.get(countTurnPattern)) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)1, Intent.ATTACK, stab, 1, false));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)2, Intent.ATTACK, stabB, 1, false));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)3, Intent.DEFEND));
                break;
        }
    }
}
