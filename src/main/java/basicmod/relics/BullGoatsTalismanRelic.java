package basicmod.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static basicmod.EldenRingSTS.makeID;

public class BullGoatsTalismanRelic extends BaseRelic implements OnReceivePowerRelic {
    private static final String NAME = "BullGoatsTalisman";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.SOLID;

    public BullGoatsTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature source) {
        if (AbstractDungeon.player.hasRelic(ID) && (power.ID.equals("Frail"))) {
            AbstractDungeon.player.getRelic(ID).flash();
            return false;
        }
        return true;
    }
}
