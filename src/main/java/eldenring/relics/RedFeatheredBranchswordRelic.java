package eldenring.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static eldenring.EldenRingSTS.makeID;

public class RedFeatheredBranchswordRelic extends BaseRelic {
    private static final String NAME = "RedFeatheredBranchsword";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int QTD = 2;
    private boolean isActive = false;

    public RedFeatheredBranchswordRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        this.isActive = false;
        this.addToBot(new AbstractGameAction() {

            @Override
            public void update() {
                if (!RedFeatheredBranchswordRelic.this.isActive && AbstractDungeon.player.isBloodied) {
                    RedFeatheredBranchswordRelic.this.flash();
                    RedFeatheredBranchswordRelic.this.pulse = true;
                    AbstractDungeon.player.addPower(new StrengthPower(AbstractDungeon.player, QTD));
                    this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, RedFeatheredBranchswordRelic.this));
                    RedFeatheredBranchswordRelic.this.isActive = true;
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
            this.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, QTD), QTD));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.isActive = true;
            AbstractDungeon.player.hand.applyPowers();
        }

    }

    @Override
    public void onNotBloodied() {
        if (this.isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;
            this.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, -QTD), -QTD));
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
