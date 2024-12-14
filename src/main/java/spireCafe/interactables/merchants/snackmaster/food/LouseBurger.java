package spireCafe.interactables.merchants.snackmaster.food;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant;

import static spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant.ZINGER_CUTOFF;

public class LouseBurger extends AbstractFoodArticle{
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(SnackmasterMerchant.ID));

    public LouseBurger(AbstractMerchant merchant, int slot) {
        super("louseBurger", merchant, slot, "louseBurger");
    }

    @Override
    public void foodEffect() {
        AbstractDungeon.topLevelEffectsQueue.add(new HealEffect(xPos, yPos, 10));
    }

    @Override
    public int getBasePrice() {
        return 75;
    }

    @Override
    public String getTipHeader() {
        return characterStrings.TEXT[ZINGER_CUTOFF+1];
    }

    @Override
    public String getTipBody() {
        return characterStrings.TEXT[ZINGER_CUTOFF+2];
    }
}
