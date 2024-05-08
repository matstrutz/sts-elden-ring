package eldenring.patches;

import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;

@SpirePatch(clz = TempMusic.class, method = "getSong")
public class BossMusicPatch {

    @SpirePostfixPatch
    public static SpireReturn<Music> Prefix(TempMusic __instance, String key) {
        switch (key) {
            case "BOSS_MORGOTT": {
                return SpireReturn.Return(MainMusic.newMusic("eldenring/audio/Morgott.ogg"));
            }
            case "BEAST_CLERGYMAN": {
                return SpireReturn.Return(MainMusic.newMusic("eldenring/audio/BeastClergyman.ogg"));
            }
            case "MALIKETH": {
                return SpireReturn.Return(MainMusic.newMusic("eldenring/audio/Maliketh.ogg"));
            }
            default: {
                return SpireReturn.Continue();
            }
        }

    }
}