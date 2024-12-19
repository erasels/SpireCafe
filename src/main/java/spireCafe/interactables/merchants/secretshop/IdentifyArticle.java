package spireCafe.interactables.merchants.secretshop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;

public class IdentifyArticle extends AbstractArticle{

    private static final int IDENTIFY_COST = 2;
    private static final String ID = Anniv7Mod.makeID(IdentifyArticle.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final Texture DISABLED_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("secretshop/identify_inactive.png"));
    private static final Texture ENABLED_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("secretshop/identify_active.png"));

    public boolean isIdentifyMode = false;
    private SecretShopMerchant ssMerchant;

    public IdentifyArticle(AbstractMerchant merchant, float x, float y) {
        super(ID, merchant, x, y, DISABLED_TEXTURE);
        this.ssMerchant = (SecretShopMerchant) merchant;
    }
    
    @Override
    public boolean canBuy() {
        return AbstractDungeon.player.currentHealth > getModifiedPrice();
    }

    @Override
    public void onClick() {
        if (!isIdentifyMode) {
            if (canBuy()) {
                isIdentifyMode = true;
                return;
            } else {
                this.ssMerchant.cantIdentify();
            }
        }

        isIdentifyMode = false;
    }

    @Override
    public void onBuy() {
        CardCrawlGame.sound.play("MONSTER_BOOK_STAB_" + MathUtils.random(0, 3));
        Wiz.p().damage(new DamageInfo(null, IDENTIFY_COST, DamageInfo.DamageType.HP_LOSS));
    }

    @Override
    public int getBasePrice() {
        return IDENTIFY_COST;
    }

    @Override
    public int getModifiedPrice() {
        return getBasePrice();
    }

    @Override
    public Texture getPriceIcon() {
        return ImageMaster.TP_HP;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (isIdentifyMode){
            itemTexture = new TextureRegion(ENABLED_TEXTURE);
        } else {
            itemTexture = new TextureRegion(DISABLED_TEXTURE);
        }
        super.render(sb);
    }

    @Override
    public String getTipHeader() {
        return uiStrings.TEXT[0];
    }

    @Override
    public String getTipBody() {
        return uiStrings.TEXT[1];
    }
    
}
