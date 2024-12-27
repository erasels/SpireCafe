package spireCafe.interactables.patrons.missingno;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenEye;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.*;
import spireCafe.util.Wiz;

import java.lang.reflect.Constructor;
import java.util.*;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.miscRng;
import static spireCafe.Anniv7Mod.*;
import static spireCafe.util.Wiz.atb;

public class MissingnoUtil {
    public static boolean isGlitched() {
        return CardCrawlGame.isInARun() && AbstractDungeon.player.hasRelic(MissingnoRelic.ID);
    }

    public static boolean isMonsterGlitched(AbstractMonster m) {
        return MissingnoPatches.GlitchedMonsterFields.isGlitched.get(m);
    }

    public static ShaderProgram initGlitchShader(ShaderProgram glitchShader) {
        if (glitchShader == null) {
            try {
                glitchShader = new ShaderProgram(
                        Gdx.files.internal(makeShaderPath("missingno/glitch/vertex.vs")),
                        Gdx.files.internal(makeShaderPath("missingno/glitch/fragment.fs"))
                );
                if (!glitchShader.isCompiled()) {
                    System.err.println(glitchShader.getLog());
                }
                if (!glitchShader.getLog().isEmpty()) {
                    System.out.println(glitchShader.getLog());
                }
            } catch (GdxRuntimeException e) {
                System.out.println("ERROR: missingno shader:");
                e.printStackTrace();
            }
        }
        return glitchShader;
    }

    public static void shuffleRelics() {
        if(CardCrawlGame.isInARun()) {
            ArrayList<AbstractRelic> relics = AbstractDungeon.player.relics;
            if (relics.size() <= 1) {
                return;
            }
            List<AbstractRelic> relicSublist = new ArrayList<>(relics.subList(1, relics.size()));
            Collections.shuffle(relicSublist);

            List<Float> currentXList = new ArrayList<>();
            List<Float> targetXList = new ArrayList<>();
            List<Hitbox> hitboxList = new ArrayList<>();

            for (int i = 1; i < relics.size(); i++) {
                currentXList.add(relics.get(i).currentX);
                targetXList.add(relics.get(i).targetX);
                hitboxList.add(relics.get(i).hb);
            }

            for (int i = 1; i < relics.size(); i++) {
                AbstractRelic newRelic = relicSublist.get(i - 1);

                relics.set(i, newRelic);

                newRelic.currentX = currentXList.get(i - 1);
                newRelic.targetX = targetXList.get(i - 1);
                newRelic.hb = hitboxList.get(i - 1);
            }
        }
    }

    public static FrameBuffer createBuffer() {
        return createBuffer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static FrameBuffer createBuffer(int sizeX, int sizeY) {
        return new FrameBuffer(Pixmap.Format.RGBA8888, sizeX, sizeY, false, false);
    }

    public static void beginBuffer(FrameBuffer fbo) {
        fbo.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glColorMask(true, true, true, true);
    }

    public static TextureRegion getBufferTexture(FrameBuffer fbo) {
        TextureRegion texture = new TextureRegion(fbo.getColorBufferTexture());
        texture.flip(false, true);
        return texture;
    }

    public static String getRandomPokeSFX() {
        return makeID("Poke") + (random.nextInt(9) + 1);
    }

    private static final List<Class<? extends AbstractGameEffect>> effectClasses = Arrays.asList(
            BiteEffect.class,
            ClashEffect.class,
            EmptyStanceEffect.class,
            EntangleEffect.class,
            ExplosionSmallEffect.class,
            FireballEffect.class,
            FlameBarrierEffect.class,
            FlickCoinEffect.class,
            GhostIgniteEffect.class,
            HeartBuffEffect.class,
            HemokinesisEffect.class,
            IceShatterEffect.class,
            LightningEffect.class,
            OmegaFlashEffect.class,
            PotionBounceEffect.class,
            PressurePointEffect.class,
            SanctityEffect.class,
            ScrapeEffect.class,
            SmallLaserEffect.class,
            StunStarEffect.class,
            ThirdEyeEffect.class,
            ViceCrushEffect.class,
            WaterDropEffect.class,
            WeightyImpactEffect.class,
            WebParticleEffect.class
    );

    public static AbstractGameEffect getRandomEffect(float x, float y) {
        int index = random.nextInt(effectClasses.size());
        Class<? extends AbstractGameEffect> chosenClass = effectClasses.get(index);

        try {
            Constructor<? extends AbstractGameEffect> constructor = findSuitableConstructor(chosenClass);
            if (constructor.getParameterCount() == 4) {
                return constructor.newInstance(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, x, y);
            } else {
                return constructor.newInstance(x, y);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Constructor<? extends AbstractGameEffect> findSuitableConstructor(Class<? extends AbstractGameEffect> clazz) throws NoSuchMethodException {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == 2 && parameterTypes[0] == float.class && parameterTypes[1] == float.class) {
                return (Constructor<? extends AbstractGameEffect>) constructor;
            } else if (parameterTypes.length == 4 &&
                    parameterTypes[0] == float.class && parameterTypes[1] == float.class &&
                    parameterTypes[2] == float.class && parameterTypes[3] == float.class) {
                return (Constructor<? extends AbstractGameEffect>) constructor;
            }
        }
        throw new NoSuchMethodException("No suitable constructor found for class: " + clazz.getName());
    }

    public static List<AbstractGameEffect> getAllEffects(float x, float y) {
        List<AbstractGameEffect> effects = new ArrayList<>();
        for (Class<? extends AbstractGameEffect> effectClass : effectClasses) {
            try {
                Constructor<? extends AbstractGameEffect> constructor = findSuitableConstructor(effectClass);
                AbstractGameEffect effect;
                if (constructor.getParameterCount() == 4) {
                    effect = constructor.newInstance(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, x, y);
                } else {
                    effect = constructor.newInstance(x, y);
                }
                effects.add(effect);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return effects;
    }

    private static boolean hasDribbled;
    private static boolean hasNameChanged;
    private static boolean hasShuffledRelics;
    private static boolean hasPlayedSfx;
    public static void doMissingnoStuff() {
        if(time > 100.0f) {
            hasDribbled = false;
            hasNameChanged = false;
            hasShuffledRelics = false;
            hasPlayedSfx = false;
            time = 0f;
        }

        if(time > 35f && !hasDribbled) {
            hasDribbled = true; //One attempt per cycle
            if (Wiz.isInCombat() && isGlitched() && miscRng.randomBoolean(.66f)) {
                if (!AbstractDungeon.player.hasRelic(FrozenEye.ID) && !AbstractDungeon.player.drawPile.isEmpty() && AbstractDungeon.player.hand.size() != BaseMod.MAX_HAND_SIZE) { //Don't be mean, only do this if it won't affect gameplay much
                    atb(new DrawCardAction(1, new DribbleCardAction()));
                }
            }
        }

        if(time > 60f && !hasNameChanged) {
            hasNameChanged = true;
            if(isGlitched() && Wiz.isInCombat() && miscRng.randomBoolean(.33f)) {
                AbstractDungeon.topPanel.setPlayerName();
            }
        }

        if(time > 80f && !hasShuffledRelics) {
            MissingnoUtil.shuffleRelics();
            hasShuffledRelics = true;
        }

        if(time > 20f && !hasPlayedSfx) {
            hasPlayedSfx = true;
            if(isGlitched() && miscRng.randomBoolean(.05f)) {
                CardCrawlGame.sound.play(getRandomPokeSFX());
            }
        }
    }
}
