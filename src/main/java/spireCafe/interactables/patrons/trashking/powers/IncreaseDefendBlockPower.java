package spireCafe.interactables.patrons.trashking.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireCafe.abstracts.AbstractSCPower;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class IncreaseDefendBlockPower extends AbstractSCPower implements InvisiblePower {
    public static final String POWER_ID = makeID(IncreaseDefendBlockPower.class.getSimpleName());
    public static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = strings.DESCRIPTIONS;

    public IncreaseDefendBlockPower(AbstractCreature owner, int blockAmount) {
        super(POWER_ID, strings.NAME, TrashKingPatron.assetID, AbstractPower.PowerType.BUFF, false, owner, blockAmount);
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if (card.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
            return blockAmount + this.amount;
        }
        return blockAmount;
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }
}
