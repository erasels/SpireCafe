package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.compendium.RelicViewScreen;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireCafe.Anniv7Mod;
import spireCafe.util.TexLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static spireCafe.Anniv7Mod.*;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.*;


public class MissingnoPatches {
    private static ShaderProgram glitchShader = null;
    private static final Texture background = TexLoader.getTexture(makeCharacterPath("Missingno/coast.png"));

    @SpirePatch(clz = AbstractPlayer.class, method = "renderPlayerImage")
    public static class ApplyPlayerShaders {
        private static FrameBuffer buffer;
        private static TextureRegion playerTexture;

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SkeletonMeshRenderer.class, "draw");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void startBuffer(AbstractPlayer __instance, SpriteBatch sb) {
            if(MissingnoUtil.isGlitched() && !Anniv7Mod.getDisableShadersConfig()) {
                sb.flush();
                if (buffer == null) {
                    buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
                }
                buffer.begin();
                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                Gdx.gl.glColorMask(true, true, true, true);
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "begin");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = LocatorTwo.class)
        public static void endBufferAndDraw(AbstractPlayer __instance, SpriteBatch sb) {
            if(MissingnoUtil.isGlitched() && !Anniv7Mod.getDisableShadersConfig()) {
                sb.flush();
                buffer.end();
                if (playerTexture == null) {
                    playerTexture = new TextureRegion(buffer.getColorBufferTexture());
                    playerTexture.flip(false, true);
                } else {
                    playerTexture.setTexture(buffer.getColorBufferTexture());
                }
                glitchShader = initGlitchShader(glitchShader);
                sb.begin();
                sb.setShader(glitchShader);
                glitchShader.setUniformf("u_time", (time % 10) + GlitchedPlayerFields.glitchOffset.get(__instance));
                glitchShader.setUniformf("u_shake_power", shake_power.get());
                glitchShader.setUniformf("u_shake_rate", shake_rate.get());
                glitchShader.setUniformf("u_shake_speed", shake_speed.get());
                glitchShader.setUniformf("u_shake_block_size", shake_block_size.get());
                glitchShader.setUniformf("u_shake_color_rate", shake_color_rate.get());


                sb.draw(playerTexture, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
                sb.setShader(null);
                sb.end();
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class GlitchedPlayerFields
    {
        public static SpireField<Integer> glitchOffset = new SpireField<>(() -> 200);

    }

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class GlitchedMonsterFields
    {
        public static SpireField<Boolean> isGlitched = new SpireField<>(() -> false);
        public static SpireField<Integer> glitchOffset = new SpireField<>(() -> 200);

    }

    @SpirePatch(clz = AbstractMonster.class, method = "render")
    public static class ApplyMonsterShaders {
        private static FrameBuffer buffer;
        private static TextureRegion monsterTexture;

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SkeletonMeshRenderer.class, "draw");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void startBuffer(AbstractMonster __instance, SpriteBatch sb) {
            if(MissingnoUtil.isMonsterGlitched(__instance) && !getDisableShadersConfig()) {
                sb.flush();
                if (buffer == null) {
                    buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
                }
                buffer.begin();
                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                Gdx.gl.glColorMask(true, true, true, true);
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "begin");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = LocatorTwo.class)
        public static void endBufferAndDraw(AbstractMonster __instance, SpriteBatch sb) {
            if(MissingnoUtil.isMonsterGlitched(__instance) && !getDisableShadersConfig()) {
                sb.flush();
                buffer.end();
                if (monsterTexture == null) {
                    monsterTexture = new TextureRegion(buffer.getColorBufferTexture());
                    monsterTexture.flip(false, true);
                } else {
                    monsterTexture.setTexture(buffer.getColorBufferTexture());
                }
                glitchShader = initGlitchShader(glitchShader);
                sb.begin();
                sb.setShader(glitchShader);
                glitchShader.setUniformf("u_time", (time % 10) + GlitchedMonsterFields.glitchOffset.get(__instance));
                glitchShader.setUniformf("u_shake_power", shake_power.get());
                glitchShader.setUniformf("u_shake_rate", shake_rate.get());
                glitchShader.setUniformf("u_shake_speed", shake_speed.get());
                glitchShader.setUniformf("u_shake_block_size", shake_block_size.get());
                glitchShader.setUniformf("u_shake_color_rate", shake_color_rate.get());


                sb.draw(monsterTexture, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
                sb.setShader(null);
                sb.end();
            }
        }
    }
    @SpirePatch(clz = AbstractRelic.class, method = "renderInTopPanel")
    public static class MissingnoRelicRender {
        @SpireInsertPatch(locator = Locator.class)
        public static void MissingnoRelicPrefixRenderPatch(AbstractRelic __instance, SpriteBatch sb) {
            if(__instance.relicId.equals(MissingnoRelic.ID)) {
                glitchShader = initGlitchShader(glitchShader);
                sb.setShader(glitchShader);
            }
        }

        @SpireInsertPatch(locator = LocatorTwo.class)
        public static void MissingnoRelicInsertPatch(AbstractRelic __instance, SpriteBatch sb) {
            if(__instance.relicId.equals(MissingnoRelic.ID)) {
                sb.setShader(null);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "setColor");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "renderCounter");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    public static final String POKE1 = makeID("Poke1");
    public static final String POKE2 = makeID("Poke2");
    public static final String POKE3 = makeID("Poke3");
    public static final String POKE4 = makeID("Poke4");
    public static final String POKE5 = makeID("Poke5");
    public static final String POKE6 = makeID("Poke6");
    public static final String POKE7 = makeID("Poke7");
    public static final String POKE8 = makeID("Poke8");
    public static final String POKE9 = makeID("Poke9");


    @SpirePatch(clz = AbstractRelic.class, method = "playLandingSFX")
    public static class PlayMissingnoSoundPatch {

        @SpirePrefixPatch
        public static SpireReturn<Void> PlaySound(AbstractRelic __instance) {
            if(__instance.relicId.equals(MissingnoRelic.ID)) {
                CardCrawlGame.sound.play(getRandomPokeSFX());
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class renderMyBg {

        @SpireInsertPatch(rloc = 11) //I'm not smart enough
        public static void MissingnoRenderBg(AbstractDungeon __instance, SpriteBatch sb) {
            if (isGlitched()) {
                float alpha = getAlpha();
                Color prevColor = sb.getColor();
                sb.setColor(1f, 1f, 1f, alpha);
                sb.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
                sb.draw(background, 0, 0, Settings.WIDTH, Settings.HEIGHT);
                sb.setColor(prevColor);
            }
        }

        private static float getAlpha() {
            float period = 5.0f;
            float pauseDuration = 60.0f;
            float maxAlpha = 0.15f;
            float totalPeriod = period + pauseDuration;
            float normalizedTime = time % totalPeriod;

            float alpha;
            if (normalizedTime < period / 2) {
                // Fade in
                float cycleTime = normalizedTime / (period / 2);
                alpha = Interpolation.linear.apply(0f, maxAlpha, cycleTime);
            } else if (normalizedTime < period) {
                // Fade out
                float cycleTime = (normalizedTime - period / 2) / (period / 2);
                alpha = Interpolation.linear.apply(maxAlpha, 0f, cycleTime);
            } else {
                // Pause
                alpha = 0.0f;
            }
            return alpha;
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "setPlayerName")
    public static class ReplaceName {

        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(Anniv7Mod.makeID("Missingno"));

        @SpireInsertPatch(locator = ReplaceName.Locator.class, localvars = {"title"} )
        public static void replaceName(TopPanel __instance, @ByRef String[] ___name, @ByRef String[] ___title) {
            if(MissingnoUtil.isGlitched()) {
                ___name[0] = jumbleName();
                ___title[0] = uiStrings.TEXT[0];
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(TopPanel.class, "titleX");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        private static String jumbleName() {
            List<Character> characters = new ArrayList<>();
            for (char c : AbstractDungeon.player.name.toCharArray()) {
                characters.add(c);
            }
            Collections.shuffle(characters);
            StringBuilder sb = new StringBuilder();
            for (char c : characters) {
                sb.append(c);
            }
            return sb.toString();
        }
    }

    @SpirePatch(clz = RelicViewScreen.class, method = "update")
    public static class ShuffleSpecialRelics {

        @SpirePostfixPatch
        public static void AtTheEnd() {
            if(time > 99.8) {
                Collections.shuffle(RelicLibrary.specialList);
            }
        }
    }
}
