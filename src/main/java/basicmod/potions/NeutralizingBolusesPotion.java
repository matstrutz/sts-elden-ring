package basicmod.potions;

import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static basicmod.EldenRingSTS.makeID;

public class NeutralizingBolusesPotion extends BasePotion {
    private static final String NAME = "Neutralizing Boluses";
    public static final String ID = makeID("NeutralizingBoluses");
    private static final PotionRarity RARITY = PotionRarity.COMMON;
    private static final PotionSize SIZE = PotionSize.HEART;
    private static final PotionColor COLOR = PotionColor.FRUIT;
    public NeutralizingBolusesPotion() {
        super(NAME, ID, RARITY, SIZE, COLOR);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new RemoveDebuffsAction(AbstractDungeon.player));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 0;
    }

    @Override
    public void initializeData() {
        this.description = "Remove ALL #yDebuff on use.";
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }
}
