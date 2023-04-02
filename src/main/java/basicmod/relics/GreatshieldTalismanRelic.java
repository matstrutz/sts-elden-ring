package basicmod.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static basicmod.EldenRingSTS.makeID;

public class GreatshieldTalismanRelic extends BaseRelic {
    private static final String NAME = "GreatshieldTalisman";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int BLOCK_PERCENT = 20;

    public GreatshieldTalismanRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onPlayerEndTurn() {
        if(AbstractDungeon.player.currentBlock > 0){
            this.flash();
            this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, Math.toIntExact(Math.round(AbstractDungeon.player.currentBlock * 1.20))));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BLOCK_PERCENT + "%" + DESCRIPTIONS[1];
    }
}
