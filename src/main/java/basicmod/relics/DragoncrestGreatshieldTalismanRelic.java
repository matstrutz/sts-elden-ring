package basicmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import static basicmod.EldenRingSTS.makeID;

public class DragoncrestGreatshieldTalismanRelic extends BaseRelic {
    private static final String NAME = "DragoncrestGreatshieldTalisman";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int PLATE_GAIN = 5;

    public DragoncrestGreatshieldTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atTurnStart() {
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, PLATE_GAIN), PLATE_GAIN));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + PLATE_GAIN + DESCRIPTIONS[1];
    }
}
