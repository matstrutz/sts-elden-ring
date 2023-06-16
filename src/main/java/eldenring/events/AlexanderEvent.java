package eldenring.events;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import eldenring.EldenRingSTS;
import org.apache.commons.lang3.StringUtils;

public class AlexanderEvent extends AbstractImageEvent {

    public static final String ID = EldenRingSTS.makeID("AlexanderStuck");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    public AlexanderEvent(){
        super(NAME, StringUtils.join(DESCRIPTIONS), "eldenring/event/Alexander01.png");

        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int optionChosen) {
        if (optionChosen == 0) {
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 10));
            openMap();
        } else {
            openMap();
        }
    }
}
