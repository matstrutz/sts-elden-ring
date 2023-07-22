package eldenring.vfx;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eldenring.util.TextureLoader;

import static basemod.helpers.VfxBuilder.Interpolations.*;

public class GlintstonePebbleVFX {

    private static final Texture BALL = TextureLoader.getTexture("eldenring/vfx/BlueCircle.png");

    //TODO CORRECT ANIMATION WITH SOUND OF DAMAGE
    //TODO FINISH ANIMATION
    public AbstractGameEffect animation(AbstractCreature from, AbstractCreature to){

        return new VfxBuilder(BALL, 0.6F)
                .scale(0.1F, 1.5F, BOUNCE)
                .setX(from.hb.cX - (from.hb.width / 2))
                .setY(from.hb.cY + (from.hb.height / 2))
                .andThen(1F)
                .moveX(from.hb.cX - (from.hb.width / 2), to.hb.cX)
                .moveY(from.hb.cY + (from.hb.height / 2) , to.hb.cY)
                .build();
    }

}
