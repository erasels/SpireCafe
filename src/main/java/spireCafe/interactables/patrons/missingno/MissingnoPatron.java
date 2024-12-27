package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.miscRng;
import static spireCafe.Anniv7Mod.*;
import static spireCafe.Anniv7Mod.shake_color_rate;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.initGlitchShader;

public class MissingnoPatron extends AbstractPatron {

    public static final String ID = MissingnoPatron.class.getSimpleName();
    public static final String assetID = "MissingnoRelic";
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static ShaderProgram glitchShader = null;
    private final float WAVY_DISTANCE = 2.0F * Settings.scale;
    private float wavy_y;
    private float wavyHelper;


    public MissingnoPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Missingno/image.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Missingno/Portrait.png")));
        this.authors = "EricB";
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1560.0F,0.0F, 0.0F, 0.0F, 0.0F);
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new MissingnoCutscene(this));
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        if(shouldShowSpeechBubble) {
            this.speechBubble.render(sb);
        }
        if(!Anniv7Mod.getDisableShadersConfig()) {
            sb.setColor(Color.WHITE);
            glitchShader = initGlitchShader(glitchShader);
            sb.setShader(glitchShader);
            glitchShader.setUniformf("u_time", (time % 10) + 200);
            glitchShader.setUniformf("u_shake_power", shake_power.get());
            glitchShader.setUniformf("u_shake_rate", shake_rate.get());
            glitchShader.setUniformf("u_shake_speed", shake_speed.get());
            glitchShader.setUniformf("u_shake_block_size", shake_block_size.get());
            glitchShader.setUniformf("u_shake_color_rate", shake_color_rate.get());
        }
        sb.draw(this.img, this.animationX - (float) this.img.getWidth() * Settings.scale / 2.0F, this.animationY + this.wavy_y, (float) this.img.getWidth() * Settings.scale, (float) this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
        if(!Anniv7Mod.getDisableShadersConfig()) {
            sb.setShader(null);
        }
        this.hitbox.render(sb);
    }


    @Override
    public void update() {
        super.update();
        this.wavyHelper += Gdx.graphics.getDeltaTime() * 2.0F;
        this.wavy_y = MathUtils.sin(this.wavyHelper) * WAVY_DISTANCE;

        if(AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(MissingnoRelic.ID) && time == 0f ) {
            if(miscRng.randomBoolean()) {
                if (miscRng.randomBoolean()) {
                    AbstractDungeon.player.gainGold(1);
                } else {
                    AbstractDungeon.player.loseGold(1);
                }
            }
        }
    }
}
