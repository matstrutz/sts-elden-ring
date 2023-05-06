package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eldenring.EldenRingSTS;

public class DragoncrestGreatshieldTalismanRelic extends BaseRelic {
    private static final String NAME = "DragoncrestGreatshieldTalisman";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int PLATE_GAIN = 5;

    private boolean firstTurn = true;

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
    }

    public DragoncrestGreatshieldTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atTurnStart() {
        if (this.firstTurn) {
            this.flash();
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, PLATE_GAIN), PLATE_GAIN));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.firstTurn = false;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + PLATE_GAIN + DESCRIPTIONS[1];
    }
}
