package eldenring.vfx;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eldenring.util.TextureLoader;

import java.util.function.BiFunction;

import static basemod.helpers.VfxBuilder.Interpolations.*;

public class GlintstonePebbleVFX {

    private static final Texture BALL = TextureLoader.getTexture("eldenring/vfx/BlueCircle.png");

    //TODO CORRECT ANIMATION WITH SOUND OF DAMAGE
    //TODO FINISH ANIMATION
    public AbstractGameEffect animation(AbstractCreature from, AbstractCreature to){
        BiFunction<Float, Float, AbstractGameEffect> tail = (x, y) -> new VfxBuilder(BALL, x, y, 0.6F)
                .setScale(0.6F)
                .moveX(x, to.hb.cX)
                .moveY(y , to.hb.cY)
                .fadeOut(0.2F)
                .build();

        return new VfxBuilder(BALL, 0.6F)
                .scale(0.1F, 1F, BOUNCE)
                .setX(from.hb.cX - (from.hb.width / 2))
                .setY(from.hb.cY + (from.hb.height / 4))
                .andThen(1F)
                .moveX(from.hb.cX - (from.hb.width / 2), to.hb.cX)
                .moveY(from.hb.cY + (from.hb.height / 4) , to.hb.cY)
                .emitEvery(tail, 0.1F)
                .build();
    }
}
