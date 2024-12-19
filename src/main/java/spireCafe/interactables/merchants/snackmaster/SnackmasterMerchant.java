package spireCafe.interactables.merchants.snackmaster;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.snackmaster.food.LouseBurger;
import spireCafe.interactables.merchants.snackmaster.food.MawFillet;
import spireCafe.interactables.merchants.snackmaster.food.SpireSpaghetti;
import spireCafe.util.TexLoader;
import spireCafe.vfx.TopLevelSpeechEffect;

public class SnackmasterMerchant extends AbstractMerchant {
    public static final String ID = SnackmasterMerchant.class.getSimpleName();
    public static final int ZINGER_CUTOFF = 6;
    public static final CharacterStrings snackmasterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    private TopLevelSpeechEffect speechEffect;

    public SnackmasterMerchant(float animationX, float animationY) {
        super(animationX, animationY, 360.0f, 235.0f);
        this.name = snackmasterStrings.NAMES[0];
        this.authors = "Gk";
        this.img = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("snackmaster/chef_with_table.png"));
        background = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeMerchantPath("snackmaster/shopscreen.png")));
    }

    @Override
    public void onInteract() {
        super.onInteract();
        if(speechEffect == null) {
            speechEffect = new TopLevelSpeechEffect(Settings.WIDTH * 0.85f, (float) Settings.HEIGHT / 2, snackmasterStrings.TEXT[MathUtils.random(ZINGER_CUTOFF)], false);
            AbstractDungeon.topLevelEffects.add(speechEffect);
        }
    }

    @Override
    public void rollShop() {
        articles.add(new LouseBurger(this, 0));
        articles.add(new MawFillet(this, 1));
        articles.add(new SpireSpaghetti(this, 2));
    }

    @Override
    public void onCloseShop() {
        speechEffect.duration = -1;
        speechEffect = null;
    }
}
