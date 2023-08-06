package eldenring.vfx;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eldenring.EldenRingSTS;
import eldenring.util.TextureLoader;

import java.util.function.BiFunction;

import static basemod.helpers.VfxBuilder.Interpolations.BOUNCE;

public class GlintstonePebbleVFX {

    private static final Texture BALL = TextureLoader.getTexture("eldenring/vfx/BlueCircle.png");

    //TODO CORRECT ANIMATION WITH SOUND OF DAMAGE
    //TODO POLISH ANIMATION
    public AbstractGameEffect animation(AbstractCreature from, AbstractCreature to){
        BiFunction<Float, Float, AbstractGameEffect> tail = (x, y) -> new VfxBuilder(BALL, x, y, 0.4F)
                .setScale(0.6F)
                .fadeOut(0.3F)
                .build();

        return new VfxBuilder(BALL, 0.4F)
                .scale(0.1F, 1F, BOUNCE)
                .setX(from.hb.cX - (from.hb.width / 2))
                .setY(from.hb.cY + (from.hb.height / 4))
                .playSoundAt(0.0F, EldenRingSTS.modID + ":GLINT_PEBBLE")
                .andThen(0.6F)
                .moveX(from.hb.cX - (from.hb.width / 2), to.hb.cX)
                .moveY(from.hb.cY + (from.hb.height / 4) , to.hb.cY)
                .emitEvery(tail, 0.1F)
                .build();
    }
}
