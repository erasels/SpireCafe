package spireCafe.screens;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.abstracts.AbstractMerchant;

public class CafeMerchantScreen extends CustomScreen {

    public AbstractMerchant currentMerchant;

    public void open(AbstractMerchant merchant) {
        AbstractDungeon.screen = curScreen();
        AbstractDungeon.isScreenUp = true;
        currentMerchant = merchant;
    }

    @Override
    public void reopen() {
        AbstractDungeon.screen = curScreen();
        AbstractDungeon.isScreenUp = true;
    }

    @Override
    public void close() {

    }

    @Override
    public void update() {
        //update Articles

        //update Close button

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        //render BG

        //render Articles

        //render Close button
    }

    @Override
    public void openingSettings() {
        AbstractDungeon.previousScreen = curScreen();
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
