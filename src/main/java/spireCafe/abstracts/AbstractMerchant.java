package spireCafe.abstracts;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;
import spireCafe.screens.CafeMerchantScreen;

import java.util.ArrayList;

public abstract class AbstractMerchant extends AbstractCafeInteractable {
    public TextureRegion background;
    public ArrayList<AbstractArticle> articles = new ArrayList<>();
    public ArrayList<AbstractArticle> toAdd = new ArrayList<>();
    public ArrayList<AbstractArticle> toRemove = new ArrayList<>();
    public boolean wasShopRolled = false;

    protected static final float SPEECH_DUR = 4.0F;
    protected static final float SPEECH_TEXT_R_X = 164.0F * Settings.scale;
    protected static final float SPEECH_TEXT_L_X = -166.0F * Settings.scale;
    protected static final float SPEECH_TEXT_Y = 126.0F * Settings.scale;
    protected ShopSpeechBubble speechBubble = null;
    protected SpeechTextEffect speechText = null;

    public AbstractMerchant(float animationX, float animationY, float hb_w, float hb_h) {
        super(animationX, animationY, hb_w, hb_h);
    }

    public void initialize() {
        if (!wasShopRolled) {
            rollShop();
            wasShopRolled = true;
        }
    }

    //Override to populate the shop by adding AbstractArticle objects to the articles list
    protected abstract void rollShop();

    @Override
    public void onInteract() {
        BaseMod.openCustomScreen(CafeMerchantScreen.ScreenEnum.CAFE_MERCHANT_SCREEN, this);
    }

    //Override if you need special behavior when an article is bought
    public void onBuyArticle(AbstractArticle article) {
        toRemove.add(article);
    }

    //Called after the custom screen is close in case you need to take care of lingering effects or something
    public void onCloseShop() {}

    public void updateShop() {
        for (AbstractArticle article : toAdd) {
            articles.add(article);
        }
        toAdd.clear();
        for (AbstractArticle article : articles) {
            article.update();
        }
        for (AbstractArticle article : toRemove) {
            articles.remove(article);
        }
        toRemove.clear();
    }

    public void renderShop(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(background, 0f,0f, Settings.WIDTH, Settings.HEIGHT);
        for (AbstractArticle article : articles) {
            article.render(sb);
        }
    }

    protected void createSpeechBubble(String msg) {
        if (this.speechBubble != null) {
            if (this.speechBubble.duration > 0.3F) {
                this.speechBubble.duration = 0.3F;
                this.speechText.duration = 0.3F;
            }
        }
        boolean isRight = MathUtils.randomBoolean();
        float x = MathUtils.random(660.0F, 1260.0F) * Settings.scale;
        float y = Settings.HEIGHT - 380.0F * Settings.scale;
        this.speechBubble = new ShopSpeechBubble(x, y, SPEECH_DUR, msg, isRight);
        float offset_x = isRight ? SPEECH_TEXT_R_X : SPEECH_TEXT_L_X;
        this.speechText = new SpeechTextEffect(x + offset_x, y + SPEECH_TEXT_Y, SPEECH_DUR, msg, DialogWord.AppearEffect.BUMP_IN);
        AbstractDungeon.topLevelEffectsQueue.add(this.speechBubble);
        AbstractDungeon.topLevelEffectsQueue.add(this.speechText);

    }

    protected void updateSpeech() {
        if (this.speechBubble != null) {
            this.speechBubble.update();
            if (this.speechBubble.hb.hovered && this.speechBubble.duration > 0.3F) {
                this.speechBubble.duration = 0.3F;
                this.speechText.duration = 0.3F;
            }
            if (this.speechBubble.isDone) {
                this.speechBubble = null;
            }
        }
        if (speechText != null) {
            this.speechText.update();
            if (this.speechText.isDone) {
                this.speechText = null;
            }
        }
    }
}
