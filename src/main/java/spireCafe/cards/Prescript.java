package spireCafe.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireCafe.abstracts.AbstractSCCard;

import static spireCafe.Anniv7Mod.makeID;

@AutoAdd.Ignore
public class Prescript extends AbstractSCCard {
    public final static String ID = makeID(Prescript.class.getSimpleName());

    public Prescript() {
        super(ID, -2, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    public void upp() {

    }
}
