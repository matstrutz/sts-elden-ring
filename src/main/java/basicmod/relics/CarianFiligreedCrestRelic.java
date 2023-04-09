package basicmod.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.List;

import static basicmod.EldenRingSTS.makeID;

public class CarianFiligreedCrestRelic extends BaseRelic {
    private static final String NAME = "CarianFiligreedCrest";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;
    private static final int COST = 0;

    private boolean costReduced = false;
    List<AbstractCard> cardList = new ArrayList<>();

    public CarianFiligreedCrestRelic() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atTurnStart(){
        costReduced = false;
    }
    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(!costReduced){
            if(AbstractCard.CardType.SKILL.equals(drawnCard.type)){
                if(drawnCard.cost > 0 && drawnCard.costForTurn > 0 && !drawnCard.freeToPlayOnce){
                    this.flash();
                    drawnCard.setCostForTurn(0);
                    costReduced = true;
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + COST;
    }
}
