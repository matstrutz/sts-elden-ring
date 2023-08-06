package eldenring.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;
import eldenring.vfx.GlintstonePebbleVFX;

public class NobleSorcererMonster extends BaseMonster {
    public final static String NAME = "Noble Sorcerer";
    public final static String FAKE_ID = "NobleSorcerer";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int glintstone = 3;

    public NobleSorcererMonster(float offX, float offY) {
        super(NAME, ID, 11, 0.0F, 0.0F, 110.0F, 210.0F, EldenRingSTS.monsterPath("NobleSorcerer"), offX, offY);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.glintstone += 1;
        }

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(12, 14);
        }

        setDmg();
    }

    private void setDmg(){
        this.damage.add(new DamageInfo(this, this.glintstone));
    }

    @Override
    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new GlintstonePebbleVFX().animation(this, AbstractDungeon.player), GlintstonePebbleVFX.DUR));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
    }

    @Override
    protected void getMove(int i) {
        this.setMove((byte) 1, Intent.ATTACK, glintstone);
    }
}
