package eldenring.events;

import basemod.devcommands.relic.RelicRemove;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.RelicTools;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eldenring.EldenRingSTS;
import eldenring.enumeration.EventChooseEnum;
import eldenring.enumeration.EventFightEnum;
import eldenring.monsters.AlexanderEventMonster;
import eldenring.relics.DaedicarsWoeRelic;
import eldenring.relics.ShardOfAlexanderRelic;
import eldenring.relics.TakersCameoRelic;
import eldenring.relics.WarriorJarShardRelic;

import java.util.*;

public class AlexanderDuelEvent extends AbstractImageEvent {

    public static final String ID = EldenRingSTS.makeID("AlexanderDuel");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    private EventFightEnum screen;

    public AlexanderDuelEvent(){
        super(NAME, DESCRIPTIONS[0] + DESCRIPTIONS[1], "eldenring/event/AlexanderDuel.png");

        this.imageEventText.setDialogOption(OPTIONS[0], !AbstractDungeon.player.hasRelic(WarriorJarShardRelic.ID), new ShardOfAlexanderRelic());
        this.imageEventText.setDialogOption(OPTIONS[1], !AbstractDungeon.player.hasRelic(WarriorJarShardRelic.ID), new WarriorJarShardRelic());
        this.imageEventText.setDialogOption(OPTIONS[2]);
        screen = EventFightEnum.INTRO;
    }

    @Override
    protected void buttonEffect(int optionChosen) {
        switch (this.screen) {
            case INTRO:
                switch (optionChosen) {
                    case 0:
                        AbstractDungeon.player.loseRelic(WarriorJarShardRelic.ID);
                        this.screen = EventFightEnum.FIGHT;
                        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(AlexanderEventMonster.ID);
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().addRelicToRewards(new ShardOfAlexanderRelic());
                        this.enterCombatFromImage();
                        AbstractDungeon.lastCombatMetricKey = "Alexander Duel";
                        break;
                    case 1:
                        AbstractDungeon.player.loseRelic(WarriorJarShardRelic.ID);
                        this.screen = EventFightEnum.LEAVE;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        break;
                    case 2:
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 12));
                        this.screen = EventFightEnum.LEAVE;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        break;
                    default:
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.screen = EventFightEnum.LEAVE;
                        break;
                }

                this.imageEventText.clearRemainingOptions();
                break;
            default:
                this.openMap();
        }
    }

}
