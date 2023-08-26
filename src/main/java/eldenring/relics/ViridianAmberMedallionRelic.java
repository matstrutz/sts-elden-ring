package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import eldenring.EldenRingSTS;

public class ViridianAmberMedallionRelic extends BaseRelic {
    private static final String NAME = "ViridianAmberMedallion";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.SHOP;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int HP_HEAL = 3;

    public ViridianAmberMedallionRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof EventRoom) {
            this.flash();
            AbstractDungeon.player.heal(HP_HEAL);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HP_HEAL + DESCRIPTIONS[1];
    }
}
