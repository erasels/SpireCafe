package spireCafe.interactables.patrons.dandaleftnut;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCCard;
import spireCafe.util.Wiz;

public class GoldenBallLightning extends AbstractSCCard {
    public static final String ID = Anniv7Mod.makeID(GoldenBallLightning.class.getSimpleName());

    public GoldenBallLightning() {
        super(ID, "DandadanPatron", 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY, CardColor.BLUE);
        baseDamage = 7;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.doDmg(m, damage, AttackEffect.SLASH_HEAVY);

        for (int i = 0; i < magicNumber; ++i) {
            Wiz.atb(new ChannelAction(new Lightning()));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }

}
