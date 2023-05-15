package eldenring.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eldenring.EldenRingSTS;

public class SpearTalismanRelic extends BaseRelic {
    private static final String NAME = "SpearTalisman";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int DMG = 1;

    public SpearTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        return (c.type.equals(AbstractCard.CardType.ATTACK) && EnergyPanel.totalCount < 1) ? damage + 1 : damage;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[2] + DMG;
    }
}
