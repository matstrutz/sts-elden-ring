package eldenring.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;

import static eldenring.EldenRingSTS.makeID;

public class AxeTalismanRelic extends BaseRelic {
    private static final String NAME = "AxeTalisman";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int DAMAGE_PERCENT = 50;

    public AxeTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        return (c.cost >= 3 && AbstractCard.CardType.ATTACK.equals(c.type)) ? (float) (damage * 1.5) : damage;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DAMAGE_PERCENT + DESCRIPTIONS[1];
    }
}
