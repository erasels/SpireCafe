package spireCafe.interactables.merchants.example;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;

public class ExampleMerchant extends AbstractMerchant {

    private static final Texture ITEM_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeUIPath("WhiteCircle.png"));
    private static final Texture BG_TEXTURE = ImageMaster.loadImage("images/npcs/rug/fra.png");

    public ExampleMerchant(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = "TEMPDOSTRINGS";
        this.img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("ExampleNPC/image.png"));
        background = new TextureRegion(BG_TEXTURE);
    }

    @Override
    public void rollShop() {
        AbstractArticle randomRare = new AbstractArticle("randomRare", this, Settings.WIDTH/2f, Settings.HEIGHT/2f, ITEM_TEXTURE) {
            @Override
            public boolean canBuy() {
                return AbstractDungeon.player.gold > price;
            }

            @Override
            public void onBuy() {
                AbstractDungeon.player.loseGold(price);
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.getCard(AbstractCard.CardRarity.RARE), xPos, yPos));
            }
        };
        randomRare.priceIcon = ImageMaster.UI_GOLD;
        randomRare.price = 50;
        articles.add(randomRare);
    }
}
