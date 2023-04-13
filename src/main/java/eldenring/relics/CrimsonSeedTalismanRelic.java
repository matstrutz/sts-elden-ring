package eldenring.relics;

import eldenring.EldenRingSTS;

public class CrimsonSeedTalismanRelic extends BaseRelic {
    private static final String NAME = "CrimsonSeedTalisman";
    public static final String ID = EldenRingSTS.makeID(NAME);
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
