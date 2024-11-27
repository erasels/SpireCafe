package spireCafe.interactables.npcs.koishi;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;

public class KoishiPatron extends AbstractPatron {
    public static final String ID = KoishiPatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    public KoishiPatron(float animationX, float animationY) {
        super(animationX, animationY, 200.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.animation = new SpriterAnimation(Anniv7Mod.makeCharacterPath("Koishi/Spriter/KoishiAnimation.scml"));
        this.animation.setFlip(true, false);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Koishi/Portrait.png")));
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1560.0F,0.0F, 0.0F, 0.0F, 0.0F);
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new KoishiCutscene(this));
    }
}
