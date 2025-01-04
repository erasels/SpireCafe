package spireCafe.interactables.patrons.dandadan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import basemod.abstracts.CustomPotion;
import spireCafe.Anniv7Mod;

public class RightballPotion extends CustomPotion {

    public static final String Potion_ID = Anniv7Mod.makeID(RightballPotion.class.getSimpleName());

    private static final PotionStrings potionStrings;
    private int returnChance;

    public RightballPotion(int returnChance) {
        this();
        this.returnChance = returnChance;
        initializeData();
    }

    public RightballPotion() {
        super(potionStrings.NAME, Potion_ID, PotionRarity.PLACEHOLDER, PotionSize.SPHERE, PotionColor.NONE);
        isThrown = true;
        targetRequired = true;
        returnChance = 100;
        initializeData();
    }

    public void initializeData() {
        this.potency = this.getPotency();
        this.description = String.format(potionStrings.DESCRIPTIONS[0], potency, returnChance);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        DamageInfo info = new DamageInfo(AbstractDungeon.player, this.potency, DamageInfo.DamageType.THORNS);
        info.applyEnemyPowersOnly(target);
        this.addToBot(new DamageAction(target, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (AbstractDungeon.cardRandomRng.randomBoolean(returnChance / 100.0f)) {
            RightballPotionPatch.rbp = this;
        }
        returnChance -= 10;
    }

    public int getPotency(int i) {
        return 20;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new RightballPotion(returnChance);
    }

    static {
        potionStrings = CardCrawlGame.languagePack.getPotionString(Potion_ID);
    }
}
