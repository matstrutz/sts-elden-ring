package eldenring.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eldenring.EldenRingSTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ErdtreesFavorRelic extends BaseRelic {
    private static final String NAME = "ErdtreesFavor";
    public static final String ID = EldenRingSTS.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int ENERGY_START = 1;
    private static final int HP_AMOUNT = 4;
    private static final int POTION_QTD = 1;
    private boolean firstTurn = true;

    public ErdtreesFavorRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(HP_AMOUNT, true);

        upgradeCardOnEquip();
    }

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
    }

    @Override
    public void atTurnStart() {
        if (this.firstTurn) {
            this.flash();
            this.addToTop(new GainEnergyAction(ENERGY_START));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.firstTurn = false;
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1] + HP_AMOUNT + DESCRIPTIONS[2] + DESCRIPTIONS[3];
    }

    public void upgradeCardOnEquip(){
        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();

        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                upgradableCards.add(c);
            }
        }

        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        if (!upgradableCards.isEmpty()) {
            if (upgradableCards.size() == 1) {
                upgradableCards.get(0).upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy()));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            } else {
                upgradableCards.get(0).upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(1));
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            }
        }
    }
}
