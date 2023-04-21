package eldenring.relics;

import eldenring.EldenRingSTS;

public class SilverScarabRelic extends BaseRelic {
    private static final String NAME = "SilverScarab";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;
    private static final int EXTRA_ATTACK = 2;

    public SilverScarabRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public int changeRareCardRewardChance(int rareCardChance) {
        return rareCardChance * 2;
    }
    @Override
    public int changeUncommonCardRewardChance(int uncommonCardChance) {
        return uncommonCardChance * 3;
    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
