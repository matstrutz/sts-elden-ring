package eldenring.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import eldenring.EldenRingSTS;
import eldenring.enumeration.EventChooseEnum;
import eldenring.relics.DaedicarsWoeRelic;
import eldenring.relics.TakersCameoRelic;

public class RyaSnakeEvent extends AbstractImageEvent {

    public static final String ID = EldenRingSTS.makeID("RyaSnake");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    private Boolean cardSelect;
    private AbstractCard offeredCard;
    private EventChooseEnum screen;

    public RyaSnakeEvent(){
        super(NAME, DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[2] + DESCRIPTIONS[3], "eldenring/event/RyaSnake.png");

        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);
        this.cardSelect = Boolean.FALSE;
        screen = EventChooseEnum.INTRO;
    }

    @Override
    protected void buttonEffect(int optionChosen) {
        if(optionChosen == 1){
            this.screen = EventChooseEnum.COMPLETE;
        }
        switch (this.screen) {
            case INTRO:
                this.screen = EventChooseEnum.CHOOSE;
                break;
            case CHOOSE:
                if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                    AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, DESCRIPTIONS[7], false, false, false, true);
                    this.imageEventText.updateBodyText(DESCRIPTIONS[4] + DESCRIPTIONS[5] + DESCRIPTIONS[6]);
                    this.cardSelect = true;
                } else {
                    this.screen = EventChooseEnum.COMPLETE;
                }
                break;
            case COMPLETE:
                this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                this.imageEventText.clearRemainingOptions();
                this.openMap();
                break;
        }
    }

    @Override
    public void update(){
        super.update();
        if (this.cardSelect && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.offeredCard = AbstractDungeon.gridSelectScreen.selectedCards.remove(0);

            switch (this.offeredCard.rarity) {
                case CURSE:
                    logMetricCardRemoval("Rya Snake", "Offered Curse", this.offeredCard);
                    getGoodOrBadRelic(0);
                    break;
                case BASIC:
                    logMetricCardRemoval("Rya Snake", "Offered Basic", this.offeredCard);
                    getGoodOrBadRelic(1);
                    break;
                case COMMON:
                    logMetricCardRemoval("Rya Snake", "Offered Common", this.offeredCard);
                    getGoodOrBadRelic(2);
                    break;
                case SPECIAL:
                    logMetricCardRemoval("Rya Snake", "Offered Special", this.offeredCard);
                    getGoodOrBadRelic(6);
                    break;
                case UNCOMMON:
                    logMetricCardRemoval("Rya Snake", "Offered Uncommon", this.offeredCard);
                    getGoodOrBadRelic(3);
                    break;
                case RARE:
                    logMetricCardRemoval("Rya Snake", "Offered Rare", this.offeredCard);
                    getGoodOrBadRelic(4);
                    break;
            }
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(this.offeredCard, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
            AbstractDungeon.player.masterDeck.removeCard(this.offeredCard);
            this.imageEventText.updateDialogOption(0, OPTIONS[1]);
            this.imageEventText.clearRemainingOptions();
            this.screen = EventChooseEnum.COMPLETE;
        }
    }

    private void getGoodOrBadRelic(int calcValue){
        AbstractRelic badRelic = new DaedicarsWoeRelic();
        AbstractRelic goodRelic = new TakersCameoRelic();
        if(((int) (Math.random() * 5)) < calcValue){
            goodRelic.instantObtain();
            goodRelic.playLandingSFX();
        } else {
            badRelic.instantObtain();
            badRelic.playLandingSFX();
        }
    }

}
