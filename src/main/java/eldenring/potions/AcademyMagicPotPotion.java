package eldenring.potions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import eldenring.EldenRingSTS;

public class AcademyMagicPotPotion extends BasePotion {
    public static final String ID = EldenRingSTS.makeID("AcademyMagicPot");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    private static final String NAME = potionStrings.NAME;
    private static final PotionRarity RARITY = PotionRarity.UNCOMMON;
    private static final PotionSize SIZE = PotionSize.BOLT;
    private static final PotionColor COLOR = PotionColor.BLUE;
    private static final int POTENCY = 30;


    public AcademyMagicPotPotion() {
        super(NAME, ID, RARITY, SIZE, COLOR);
        this.isThrown = true;
        this.targetRequired = true;
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        DamageInfo info = new DamageInfo(AbstractDungeon.player, calcPotencyWithRelic(POTENCY), DamageInfo.DamageType.THORNS);
        info.applyEnemyPowersOnly(abstractCreature);
        this.addToBot(new DamageAction(abstractCreature, info, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return POTENCY;
    }

    @Override
    public void initializeData() {
        this.potency = calcPotencyWithRelic(POTENCY);
        this.description = potionStrings.DESCRIPTIONS[0] + POTENCY + potionStrings.DESCRIPTIONS[1];
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
