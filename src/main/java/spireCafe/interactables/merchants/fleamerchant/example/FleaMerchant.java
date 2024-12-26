package spireCafe.interactables.merchants.fleamerchant.example;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Intimidate;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.potions.Ambrosia;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.NlothsGift;
import com.megacrit.cardcrawl.relics.PenNib;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.CardArticle;
import spireCafe.interactables.merchants.PotionArticle;
import spireCafe.interactables.merchants.RelicArticle;
import spireCafe.util.TexLoader;

public class FleaMerchant extends AbstractMerchant {
    public static final String ID = FleaMerchant.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    private static final Texture ITEM_TEXTURE = TexLoader.getTexture("images/relics/penNib.png");
    private static final Texture BG_TEXTURE = ImageMaster.loadImage("images/npcs/rug/fra.png");

    public FleaMerchant(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Jack Renoson";
        this.img = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("example/merchant.png"));
        background = new TextureRegion(BG_TEXTURE);
    }

    @Override
    public void rollShop() {
        //The X and Y positions for the articles are the bottom left spot where the image and its hitbox will start. The cost will be rendered below.

        //Use CardArticle to add cards to your shop. You can override onBuy() and getPriceIcon() to change the price from gold (default) to something else
        AbstractArticle intimidate = new CardArticle("intimidate", this, 320f * Settings.xScale,700f * Settings.yScale, new Intimidate(), 75);
        articles.add(intimidate);

        //Use RelicArticle to add relics to your shop. You can override onBuy() and getPriceIcon() to change the price from gold (default) to something else
        AbstractArticle nlothGift = new RelicArticle("nlothgift", this, 500f * Settings.xScale, 700f * Settings.yScale, new NlothsGift(), 125);
        articles.add(nlothGift);

        //Use PotionArticle to add potions to your shop. You can override onBuy() and getPriceIcon() to change the price from gold (default) to something else (unlike the others, onBuy ONLY handles the price)
        AbstractArticle potion = new PotionArticle("potion", this, 620f * Settings.xScale, 700f * Settings.yScale, new Ambrosia(), 125);
        articles.add(potion);

        //Use AbstractArticle directly or make a class that extends it for all other use cases. Note that onBuy() must handle the price AND what is being bought
        AbstractArticle primedRelicArticle = new AbstractArticle("primedRelic", this, Settings.WIDTH/2f, Settings.HEIGHT/2f, ITEM_TEXTURE) {
            @Override
            public boolean canBuy() {
                return AbstractDungeon.player.gold > getModifiedPrice();
            }

            @Override
            public void onBuy() {
                AbstractRelic relic = new PenNib();
                relic.setCounter(9);
                AbstractDungeon.player.loseGold(getModifiedPrice());
                AbstractDungeon.getCurrRoom().relics.add(relic);
                relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
                relic.flash();
            }

            @Override
            public String getTipHeader() {
                return characterStrings.TEXT[0] + PenNib.ID;
            }

            @Override
            public String getTipBody() {
                return characterStrings.TEXT[1];
            }

            @Override
            public int getBasePrice() {
                return 250;
            }
        };
        articles.add(primedRelicArticle);


    }
}
