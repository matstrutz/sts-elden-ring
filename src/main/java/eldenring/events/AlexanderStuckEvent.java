package eldenring.events;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eldenring.EldenRingSTS;
import eldenring.relics.WarriorJarShardRelic;
import org.apache.commons.lang3.StringUtils;

public class AlexanderStuckEvent extends AbstractImageEvent {

    public static final String ID = EldenRingSTS.makeID("AlexanderStuck");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    private int dmg_take = 20;

    public AlexanderStuckEvent(){
        super(NAME, DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[2] + DESCRIPTIONS[3] + DESCRIPTIONS[4] + DESCRIPTIONS[5], "eldenring/event/AlexanderStuck.png");

        dmg_take += calcDmgTake();

        this.imageEventText.setDialogOption(OPTIONS[0] + dmg_take + OPTIONS[1]);
        this.imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int optionChosen) {
        if (optionChosen == 0) {
            if(!AbstractDungeon.player.hasRelic(WarriorJarShardRelic.ID)){
                AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, dmg_take));
                AbstractRelic relic = new WarriorJarShardRelic();
                relic.instantObtain();
                relic.playLandingSFX();
            }
            this.imageEventText.updateBodyText(DESCRIPTIONS[6] + DESCRIPTIONS[7] + DESCRIPTIONS[8]);

            this.imageEventText.updateDialogOption(0, OPTIONS[2]);
            this.imageEventText.clearRemainingOptions();
            openMap();
        } else {
            openMap();
        }
    }

    private int calcDmgTake(){
        int dmg = 0;

        for (int i = 0; i < AbstractDungeon.ascensionLevel; i++) {
            dmg++;
        }

        return dmg;
    }
}
