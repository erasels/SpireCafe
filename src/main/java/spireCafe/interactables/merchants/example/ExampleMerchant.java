package spireCafe.interactables.merchants.example;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Discovery;
import com.megacrit.cardcrawl.cards.red.Intimidate;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.CardArticle;
import spireCafe.interactables.npcs.example.ExamplePatron;
import spireCafe.util.TexLoader;

public class ExampleMerchant extends AbstractMerchant {
    public static final String ID = ExampleMerchant.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    private static final Texture ITEM_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeUIPath("RandomRareArticle.png"));
    private static final Texture BG_TEXTURE = ImageMaster.loadImage("images/npcs/rug/fra.png");

    public ExampleMerchant(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("ExampleNPC/image.png"));
        background = new TextureRegion(BG_TEXTURE);
    }

    @Override
    public void rollShop() {
        AbstractArticle randomRare = new AbstractArticle("randomRare", this, Settings.WIDTH/2f, Settings.HEIGHT/2f, ITEM_TEXTURE) {
            @Override
            public boolean canBuy() {
                return AbstractDungeon.player.gold > getModifiedPrice();
            }

            @Override
            public void onBuy() {
                AbstractDungeon.player.loseGold(getModifiedPrice());
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.getCard(AbstractCard.CardRarity.RARE), xPos, yPos));
            }

            @Override
            public String getTipHeader() {
                return characterStrings.TEXT[0];
            }

            @Override
            public String getTipBody() {
                return characterStrings.TEXT[1];
            }

            @Override
            public int getBasePrice() {
                return 50;
            }
        };
        articles.add(randomRare);

        AbstractArticle intimidate = new CardArticle("intimidate", this, 300f * Settings.xScale,700f * Settings.xScale, new Intimidate(), 75);
        articles.add(intimidate);
    }
}
