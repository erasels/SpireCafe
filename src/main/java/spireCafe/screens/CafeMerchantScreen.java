package spireCafe.screens;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractMerchant;

public class CafeMerchantScreen extends CustomScreen {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(Anniv7Mod.makeID("CafeUI"));

    public AbstractMerchant currentMerchant;

    public void open(AbstractMerchant merchant) {
        AbstractDungeon.screen = curScreen();
        AbstractDungeon.isScreenUp = true;
        currentMerchant = merchant;
        AbstractDungeon.overlayMenu.cancelButton.show(uiStrings.TEXT[0]);
        AbstractDungeon.overlayMenu.hideBlackScreen();

    }

    @Override
    public void reopen() {
        AbstractDungeon.screen = curScreen();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.cancelButton.show(uiStrings.TEXT[0]);
        AbstractDungeon.overlayMenu.hideBlackScreen();
    }

    @Override
    public void close() {
        AbstractDungeon.screen = AbstractDungeon.previousScreen;
        AbstractDungeon.isScreenUp = false;
        AbstractDungeon.overlayMenu.hideBlackScreen();
        currentMerchant.onCloseShop();
    }


    @Override
    public void update() {
        currentMerchant.updateShop();

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        currentMerchant.renderShop(spriteBatch);
    }

    @Override
    public void openingSettings() {
        AbstractDungeon.previousScreen = curScreen();
    }

    @Override
    public boolean allowOpenDeck() {
        return true;
    }

    @Override
    public void openingDeck() {
        AbstractDungeon.previousScreen = curScreen();
    }

    @Override
    public boolean allowOpenMap() {
        return false;
    }



    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return ScreenEnum.CAFE_MERCHANT_SCREEN;
    }

    public static class ScreenEnum {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen CAFE_MERCHANT_SCREEN;
    }
}
