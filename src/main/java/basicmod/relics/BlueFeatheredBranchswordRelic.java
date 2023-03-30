package basicmod.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static basicmod.EldenRingSTS.makeID;

public class BlueFeatheredBranchswordRelic extends BaseRelic {
    private static final String NAME = "BlueFeatheredBranchsword";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int QTD = 2;
    private boolean isActive = false;

    public BlueFeatheredBranchswordRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        this.isActive = false;
        this.addToBot(new AbstractGameAction() {

            @Override
            public void update() {
                if (!BlueFeatheredBranchswordRelic.this.isActive && AbstractDungeon.player.isBloodied) {
                    BlueFeatheredBranchswordRelic.this.flash();
                    BlueFeatheredBranchswordRelic.this.pulse = true;
                    AbstractDungeon.player.addPower(new DexterityPower(AbstractDungeon.player, QTD));
                    this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, BlueFeatheredBranchswordRelic.this));
                    BlueFeatheredBranchswordRelic.this.isActive = true;
                    AbstractDungeon.onModifyPower();
                }

                this.isDone = true;
            }
        });
    }

    @Override
    public void onBloodied() {
        this.flash();
        this.pulse = true;
        if (!this.isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;
            this.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, QTD), QTD));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.isActive = true;
            AbstractDungeon.player.hand.applyPowers();
        }

    }

    @Override
    public void onNotBloodied() {
        if (this.isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;
            this.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, -QTD), -QTD));
        }

        this.stopPulse();
        this.isActive = false;
        AbstractDungeon.player.hand.applyPowers();
    }

    @Override
    public void onVictory() {
        this.pulse = false;
        this.isActive = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + QTD + DESCRIPTIONS[1];
    }
}
