package spireCafe.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireCafe.Anniv7Mod;

public class WornMod extends AbstractCardModifier {
    public static String ID = Anniv7Mod.makeID(WornMod.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private final float damageMod;
    private final float blockMod;
    private final int magicDown;

    public WornMod(float damageMod, float blockMod, int magicDown) {
        this.damageMod = damageMod;
        this.blockMod = blockMod;
        this.magicDown = magicDown;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseDamage = (int) (card.baseDamage * damageMod);
        card.baseBlock = (int) (card.baseBlock * blockMod);
        card.baseMagicNumber -= magicDown;
        card.rarity = AbstractCard.CardRarity.COMMON;
        card.name = uiStrings.TEXT[0].replace("{0}", card.name);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new WornMod(damageMod, blockMod, magicDown);
    }
}
