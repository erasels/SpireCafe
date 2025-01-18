package spireCafe.interactables.merchants.griddraft;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;
import spireCafe.util.Wiz;

public class GridDraftMerchant extends AbstractMerchant{

    private static final String ID = GridDraftMerchant.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static final String[] TEXT = characterStrings.TEXT;
    private static final Texture BG_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("griddraft/rug.png"));
    private static float HB_W = 160.0F;
    private static float HB_H = 200.0F;
    private AbstractArticle[][] shopGrid;

    public GridDraftMerchant(float animationX, float animationY) {
        super(animationX, animationY, HB_W, HB_H);
        this.shopGrid = new AbstractArticle[3][3];
        this.name = characterStrings.NAMES[0];
        this.authors = "Coda";
        this.img = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("example/merchant.png"));
        background = new TextureRegion(BG_TEXTURE);
    }

    @Override
    protected void rollShop() {
        for (int i=0;i<3;i++) {
            for (int j=0;j<3;j++){
                AbstractCard card = Wiz.returnTrulyRandomRarityCardInCombat(CardRarity.UNCOMMON);
                // card.drawScale = GridCardArticle.CARD_SCALE;
                AbstractArticle gridCardArticle = new GridCardArticle(this, i, j, card, 100);
                this.shopGrid[i][j] = gridCardArticle;
                articles.add(gridCardArticle);
            }
        }
    }
    
}
