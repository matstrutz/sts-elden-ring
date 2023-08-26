package eldenring.vfx;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eldenring.EldenRingSTS;
import eldenring.util.TextureLoader;

import java.util.function.BiFunction;

import static basemod.helpers.VfxBuilder.Interpolations.BOUNCE;
import static basemod.helpers.VfxBuilder.Interpolations.LINEAR;

public class SacredPhalanxVFX {
    public final static String ID = EldenRingSTS.makeID("GLINT_PEBBLE");
    public static final float DURAT = 0.6F;
    private static final Texture EFFECT = TextureLoader.getTexture("eldenring/vfx/SacredPhalanx.png");

    //TODO ADD CORRECT CUSTOM SOUND
    public AbstractGameEffect animation(AbstractCreature position){

        return new VfxBuilder(EFFECT, DURAT)
                .fadeOut(0.5F)
                .setX(position.hb.cX)
                .setY(position.hb.cY - (position.hb.height / 2))
                .playSoundAt(0.0F, EldenRingSTS.modID + ":CERULEAN_TEARS")
                .build();
    }
}
