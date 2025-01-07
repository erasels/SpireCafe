package spireCafe.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.relics.SacredBark;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Arrays;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.merchantRng;

public class PotencySaverPatch {

    @SpirePatch(clz = AbstractPotion.class, method = SpirePatch.CLASS)
    public static class PotionUseField {
        public static SpireField<Integer> isDepleted = new SpireField<>(() -> -1);
    }

    @SpirePatch(clz = AbstractPotion.class, method = "getPotency", paramtypez = {})
    public static class IncreasePotency {
        @SpireInsertPatch(locator = Locator.class, localvars = {"potency"})
        public static void patch(AbstractPotion __instance, @ByRef int[] potency) {
            if (PotionUseField.isDepleted.get(__instance) != -1) {
                potency[0] = PotionUseField.isDepleted.get(__instance);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
