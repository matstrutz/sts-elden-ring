package eldenring.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eldenring.EldenRingSTS;

public class ShieldGreasePotion extends BasePotion {
    private static final String NAME = "Shield Grease";
    public static final String ID = EldenRingSTS.makeID("ShieldGrease");
    private static final PotionRarity RARITY = PotionRarity.COMMON;
    private static final PotionSize SIZE = PotionSize.S;
    private static final PotionColor COLOR = PotionColor.WHITE;
    private static final int POTENCY = 2;
    public ShieldGreasePotion() {
        super(NAME, ID, RARITY, SIZE, COLOR);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        AbstractCreature target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new DexterityPower(target, calcPotencyWithRelic(POTENCY)), calcPotencyWithRelic(POTENCY)));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return POTENCY;
    }

    @Override
    public void initializeData() {
        this.potency = calcPotencyWithRelic(POTENCY);
        this.description = "Gain " + POTENCY + " #yDexterity.";
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    private int calcPotencyWithRelic(int potenc){
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("SacredBark")) {
            potenc *= 2;
        }

        return potenc;
    }
}
