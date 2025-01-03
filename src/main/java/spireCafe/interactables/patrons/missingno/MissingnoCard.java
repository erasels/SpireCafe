package spireCafe.interactables.patrons.missingno;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.BufferUtils;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCCard;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;
import static spireCafe.Anniv7Mod.*;
import static spireCafe.interactables.patrons.missingno.MarkovChain.MarkovType.CARD;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.createBuffer;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.hasUpdatedCardText;
import static spireCafe.util.CardArtRoller.computeCard;
import static spireCafe.util.Wiz.atb;

public class MissingnoCard extends AbstractSCCard {

    public static final String ID = makeID(MissingnoCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public MissingnoCard() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, SelfOrEnemyTargeting.SELF_OR_ENEMY);
        exhaust = true;
        String markovText = MarkovChain.getInstance(CARD).generateText(5, 15).replaceAll("[~@]", "").replaceAll("#.", "");
        rawDescription = cardStrings.DESCRIPTION + markovText;

        // Here begins the art roller skullduggery
        if (CardLibrary.cards != null && !CardLibrary.cards.isEmpty()) {
            computeCard(this, true);
            needsArtRefresh = false;
        }
        baseMagicNumber = magicNumber = random(1, 5);
        baseDamage = damage = random(6, 12);
        baseBlock = block = random(3, 8);
        initializeDescription();
    }

    @Override
    public void upp() {

    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        atb(new DrawCardAction(1));

        if(target instanceof AbstractMonster) {
            MissingnoPatches.GlitchedMonsterFields.isGlitched.set(target, true);
            MissingnoPatches.GlitchedMonsterFields.glitchOffset.set(target, random.nextInt(200));
        } else {
            MissingnoPatches.GlitchedPlayerFields.glitchOffset.set(target, MissingnoPatches.GlitchedPlayerFields.glitchOffset.get(abstractPlayer) + random.nextInt(100));
        }
        atb(new VFXAction(MissingnoUtil.getRandomEffect(target.hb.cX, target.hb.cY)));
    }

    @Override
    public void update() {
        super.update();

        //Every 3 seconds redo description
        if(((int) time) % 3 == 0 && !hasUpdatedCardText) {
            String markovText = MarkovChain.getInstance(CARD).generateText(5, 15).replaceAll("[~@]", "").replaceAll("#.", "");
            rawDescription = cardStrings.DESCRIPTION + markovText;
            baseMagicNumber = magicNumber = random(1, 5);
            baseDamage = damage = random(6, 12);
            baseBlock = block = random(3, 8);
            initializeDescription();
            hasUpdatedCardText = true;
        }
    }


    @SpirePatch(clz = AbstractCard.class, method = "render", paramtypez = SpriteBatch.class)
    public static class GlitchCardPatches {
        public static ShaderProgram glitchShader = null;
        private static final FrameBuffer fbo = createBuffer();

        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard __instance, SpriteBatch spriteBatch) {
            if(glitchShader == null) {
                glitchShader = MissingnoUtil.initGlitchShader(glitchShader);
            }
            if (!Settings.hideCards) {
                if (__instance.cardID.equals(MissingnoCard.ID) && !Anniv7Mod.getDisableShadersConfig()) {
                    TextureRegion t = cardToTextureRegion(__instance, spriteBatch);
                    spriteBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
                    ShaderProgram oldShader = spriteBatch.getShader();
                    spriteBatch.setShader(glitchShader);
                    glitchShader.setUniformf("u_time", (time % 10) + random.nextInt(5));
                    glitchShader.setUniformf("u_shake_power", shake_power.get());
                    glitchShader.setUniformf("u_shake_rate", shake_rate.get());
                    glitchShader.setUniformf("u_shake_speed", shake_speed.get());
                    glitchShader.setUniformf("u_shake_block_size", shake_block_size.get());
                    glitchShader.setUniformf("u_shake_color_rate", shake_color_rate.get());

                    spriteBatch.draw(t, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
                    spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                    spriteBatch.setShader(oldShader);
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }

        public static TextureRegion cardToTextureRegion(AbstractCard card, SpriteBatch sb) {
            sb.end();
            MissingnoUtil.beginBuffer(fbo);
            sb.begin();
            IntBuffer buf_rgb = BufferUtils.newIntBuffer(16);
            IntBuffer buf_a = BufferUtils.newIntBuffer(16);
            Gdx.gl.glGetIntegerv(GL30.GL_BLEND_EQUATION_RGB, buf_rgb);
            Gdx.gl.glGetIntegerv(GL30.GL_BLEND_EQUATION_ALPHA, buf_a);

            Gdx.gl.glBlendEquationSeparate(buf_rgb.get(0), GL30.GL_MAX);
            Gdx.gl.glBlendEquationSeparate(GL30.GL_FUNC_ADD, GL30.GL_MAX);
            card.render(sb, false);
            Gdx.gl.glBlendEquationSeparate(GL30.GL_FUNC_ADD, GL30.GL_FUNC_ADD);
            Gdx.gl.glBlendEquationSeparate(buf_rgb.get(0), buf_a.get(0));

            sb.end();
            fbo.end();
            sb.begin();
            return MissingnoUtil.getBufferTexture(fbo);
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "render", paramtypez = SpriteBatch.class)
    public static class MissingnoSCV {
        public static ShaderProgram glitchShader = null;

        private static ShaderProgram oldShader;

        @SpireInsertPatch(locator = MissingnoCard.MissingnoSCV.Locator.class)
        public static void ApplyShader(SingleCardViewPopup __instance, SpriteBatch sb) {
            if(glitchShader == null) {
                glitchShader = MissingnoUtil.initGlitchShader(glitchShader);
            }
            AbstractCard card = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
            if (card.cardID.equals(MissingnoCard.ID) && !getDisableShadersConfig()) {
                oldShader = sb.getShader();
                sb.setShader(glitchShader);
                glitchShader.setUniformf("u_time", (time % 10) + random.nextInt(5));
                glitchShader.setUniformf("u_shake_power", shake_power.get());
                glitchShader.setUniformf("u_shake_rate", shake_rate.get());
                glitchShader.setUniformf("u_shake_speed", shake_speed.get());
                glitchShader.setUniformf("u_shake_block_size", shake_block_size.get());
                glitchShader.setUniformf("u_shake_color_rate", shake_color_rate.get());
            }
        }

        @SpireInsertPatch(locator = MissingnoCard.MissingnoSCV.LocatorTwo.class)
        public static void RemoveShader(SingleCardViewPopup __instance, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
            if (card.cardID.equals(MissingnoCard.ID)) {
                sb.setShader(oldShader);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderCardBack");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderArrows");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }
}