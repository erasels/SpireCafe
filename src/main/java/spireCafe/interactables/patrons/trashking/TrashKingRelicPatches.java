package spireCafe.interactables.patrons.trashking;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.interactables.patrons.trashking.powers.PaperNailPower;
import spireCafe.interactables.patrons.trashking.relics.*;

public class TrashKingRelicPatches {
    private static boolean isApplyingSupplement = false;

    @SpirePatch(
            clz = ConstrictedPower.class,
            method = "atEndOfTurn",
            paramtypez = {boolean.class}
    )
    public static class ConstrictedPowerPatch {
        @SpirePostfixPatch
        public static void Postfix(ConstrictedPower __instance, boolean isPlayer) {
            if (isPlayer && __instance.owner.isPlayer) {
                AbstractRelic life = AbstractDungeon.player.getRelic(Life.ID);
                if (life != null && __instance.amount > 0) {
                    int hpBefore = __instance.owner.currentHealth;
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            this.isDone = true;
                            int hpAfter = __instance.owner.currentHealth;
                            if (hpAfter < hpBefore) {
                                life.flash();
                                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
                            }
                        }
                    });
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "loseGold"
    )
    public static class GoldLossPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer _instance, int goldAmount) {
            if (goldAmount >= 100 && _instance instanceof AbstractPlayer) {
                AbstractPlayer player = (AbstractPlayer) _instance;
                if (player.hasRelic(PaintBucket.ID)) {
                    player.getRelic(PaintBucket.ID).flash();
                    player.increaseMaxHp(1, true);
                }
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "removeCard",
            paramtypez = {AbstractCard.class}
    )
    public static class HeirloomForkPatch {
        @SpirePostfixPatch
        public static void Postfix(CardGroup _instance, AbstractCard c) {
            if (_instance.type == CardGroup.CardGroupType.MASTER_DECK && c.type == AbstractCard.CardType.CURSE && AbstractDungeon.player.hasRelic(HeirloomFork.ID)) {
                ((HeirloomFork) AbstractDungeon.player.getRelic(HeirloomFork.ID)).onRemoveCurseFromDeck();
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "decreaseMaxHealth"
    )
    public static class HospitalBillPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature __instance, int amount) {
            if (__instance instanceof AbstractPlayer) {
                AbstractPlayer player = (AbstractPlayer) __instance;
                AbstractRelic hospitalBill = player.getRelic(HospitalBill.ID);
                if (hospitalBill != null) {
                    int missingHealth = player.maxHealth - player.currentHealth;
                    if (missingHealth > 0) {
                        player.heal(missingHealth);
                        hospitalBill.flash();
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "heal",
            paramtypez = {int.class, boolean.class}
    )
    public static class PaperNailPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature _instance, int healAmount, boolean showEffect) {
            if (_instance == AbstractDungeon.player && AbstractDungeon.player.hasPower(PaperNailPower.POWER_ID)) {
                ((PaperNailPower) AbstractDungeon.player.getPower(PaperNailPower.POWER_ID)).checkAndRemovePower();
            }
        }
    }

    @SpirePatch(
            clz = SmokeBomb.class,
            method = "use"
    )
    public static class SmokeBombPatch {
        @SpirePostfixPatch
        public static void Postfix(SmokeBomb __instance, AbstractCreature target) {
            AbstractRelic artOfPeace = AbstractDungeon.player.getRelic(ArtOfPeace.ID);
            if (artOfPeace != null) {
                artOfPeace.flash();
                CardCrawlGame.sound.play("GOLD_GAIN");
                AbstractDungeon.player.gainGold(50);
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "increaseMaxHp",
            paramtypez = {int.class, boolean.class}
    )
    public static class SupplementsPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature __instance, int amount, boolean showEffect) {
            if (__instance instanceof AbstractPlayer && !isApplyingSupplement) {
                AbstractPlayer player = (AbstractPlayer) __instance;
                AbstractRelic supplements = player.getRelic(Supplements.ID);
                if (supplements != null) {
                    isApplyingSupplement = true;
                    player.increaseMaxHp(1, false);
                    supplements.flash();
                    isApplyingSupplement = false;
                }
            }
        }
    }
}