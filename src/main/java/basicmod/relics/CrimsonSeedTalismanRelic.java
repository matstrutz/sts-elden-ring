package basicmod.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static basicmod.EldenRingSTS.makeID;

public class CrimsonSeedTalismanRelic extends BaseRelic {
    private static final String NAME = "CrimsonSeedTalisman";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;
    private static final int HEAL_AMOUNT = 2;

    public CrimsonSeedTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public int onPlayerHeal(int healAmount) {
        return healAmount + HEAL_AMOUNT;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HEAL_AMOUNT + DESCRIPTIONS[1];
    }
}
