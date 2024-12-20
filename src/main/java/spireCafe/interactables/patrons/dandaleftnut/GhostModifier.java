package spireCafe.interactables.patrons.dandaleftnut;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier;
import spireCafe.Anniv7Mod;
import spireCafe.util.TexLoader;

public class GhostModifier extends AbstractCardModifier {

    public static final String ID = Anniv7Mod.makeID(GhostModifier.class.getSimpleName());
    private static final UIStrings uiStrings;

    private float alpha;
    private boolean fading = true;

    public static final float FADE_SPEED = 0.3F;

    private Texture ghost_img;
    private static Texture crab_img = TexLoader.getTexture(Anniv7Mod.makeImagePath("characters/Dandaleftnut/crab.png"));
    private static Texture granny_img = TexLoader
            .getTexture(Anniv7Mod.makeImagePath("characters/Dandaleftnut/turbo_granny.png"));
    private static Texture silky_img = TexLoader
            .getTexture(Anniv7Mod.makeImagePath("characters/Dandaleftnut/acrobatic_silky.png"));

    public GhostModifier() {
    }

    public GhostModifier(int ghost) {
        switch (ghost) {
            case 0:
                ghost_img = crab_img;
                break;
            case 1:
                ghost_img = granny_img;
                break;
            case 2:
                ghost_img = silky_img;
                break;
        }
    }

    public GhostModifier(Texture img) {
        ghost_img = img;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.isEthereal = false;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return String.format(uiStrings.TEXT[0], rawDescription);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new GhostModifier(ghost_img);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.isEthereal = true;

        if (ghost_img == null) {
            switch (new java.util.Random().nextInt(3)) {
                case 0:
                    ghost_img = crab_img;
                    break;
                case 1:
                    ghost_img = granny_img;
                    break;
                case 2:
                    ghost_img = silky_img;
                    break;
            }
        }
    }

    // called by patch
    public void render(AbstractCard card, SpriteBatch sb) {
        if (alpha < 0.45F) {
            fading = false;
        } else if (alpha > 1.0F) {
            fading = true;
        }

        if (fading) {
            alpha -= FADE_SPEED * Gdx.graphics.getDeltaTime();
        } else {
            alpha += FADE_SPEED * Gdx.graphics.getDeltaTime();
        }
        renderHelper(sb, new TextureAtlas.AtlasRegion(ghost_img, 0, 0, ghost_img.getWidth(), ghost_img.getHeight()),
                card.current_x, card.current_y, card.drawScale, card.angle);
    }

    private void renderHelper(SpriteBatch sb, TextureAtlas.AtlasRegion img, float drawX, float drawY, float drawScale,
            float angle) {
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, alpha));
        sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2.0F,
                drawY + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX,
                (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight,
                drawScale * Settings.scale, drawScale * Settings.scale, angle);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("basemod:EtherealCardModifier");
    }
}
