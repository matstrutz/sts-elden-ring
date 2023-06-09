package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eldenring.EldenRingSTS;

public class RadagonsSoresealRelic extends BaseRelic {
    private static final String NAME = "RadagonsSoreseal";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int ENERGY_START = 1;
    private static final int HP_AMOUNT = 7;
    private static final int STR = 2;
    private static final int DEX = 2;
    private static final int ENEMY_STR = 4;
    private boolean firstTurn = true;

    public RadagonsSoresealRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(HP_AMOUNT, true);

    }

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
    }

    @Override
    public void atTurnStart() {
        if (this.firstTurn) {
            this.flash();
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, STR), STR));
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, DEX), DEX));
            this.addToTop(new GainEnergyAction(ENERGY_START));

            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                this.addToTop(new ApplyPowerAction(m, m, new StrengthPower(m, ENEMY_STR), ENEMY_STR));
            }
            this.firstTurn = false;
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HP_AMOUNT +
                DESCRIPTIONS[1] + STR +
                DESCRIPTIONS[2] + DESCRIPTIONS[3] + DEX +
                DESCRIPTIONS[4] + DESCRIPTIONS[5] + DESCRIPTIONS[6] +
                DESCRIPTIONS[7] + ENEMY_STR + DESCRIPTIONS[8];
    }
}
