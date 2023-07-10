package eldenring.events.conditions;

import basemod.eventUtil.util.Condition;
import basemod.patches.com.megacrit.cardcrawl.characters.AbstractPlayer.SeenEvents;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.events.AlexanderStuckEvent;

public class AlexanderStuckCondition implements Condition {

    @Override
    public boolean test() {
        return !SeenEvents.seenEvents.get(AbstractDungeon.player).contains(AlexanderStuckEvent.ID);
    }
}
