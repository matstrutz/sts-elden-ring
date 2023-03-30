package basicmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static basicmod.EldenRingSTS.makeID;

public class DaedicarsWoeRelic extends BaseRelic {
    private static final String NAME = "DaedicarsWoe";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int STR_GAIN = 2;

    public DaedicarsWoeRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atTurnStart() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            this.addToTop(new ApplyPowerAction(m, m, new StrengthPower(m, STR_GAIN), STR_GAIN));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STR_GAIN + DESCRIPTIONS[1];
    }
}