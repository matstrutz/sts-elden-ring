package basicmod.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnLoseBlockRelic;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static basicmod.EldenRingSTS.makeID;

public class CurvedSwordTalismanRelic extends BaseRelic implements OnLoseBlockRelic {
    private static final String NAME = "CurvedSwordTalisman";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int DAMAGE_PERCENT = 50;

    public CurvedSwordTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public int onLoseBlock(DamageInfo damageInfo, int i) {
        int returnDamage = (i - AbstractDungeon.player.currentBlock) / 2;
        this.flash();
        this.addToBot(new DamageAction(damageInfo.owner, new DamageInfo(AbstractDungeon.player, returnDamage, DamageInfo.DamageType.NORMAL)));
        return i;
    }
}
