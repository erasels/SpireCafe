package spireCafe.interactables.patrons.acidslimeb;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.councilofghosts.BlankTombstone;
import spireCafe.interactables.patrons.councilofghosts.CouncilOfGhostsPatron;
import spireCafe.interactables.patrons.purpletear.Prescript;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.purpletear.PurpleTearPatron.assetID;
import static spireCafe.util.Wiz.*;

public class HandfulOfSlime extends AbstractSCRelic {
    public static final String ID = makeID(HandfulOfSlime.class.getSimpleName());
    private static final int TURNS = 1;

    public HandfulOfSlime() {
        super(ID, AcidSlimeBPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.SOLID);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, (AbstractCreature)null, new IntangiblePlayerPower(AbstractDungeon.player, TURNS), TURNS));
        grayscale = true;
    }

    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TURNS + DESCRIPTIONS[1];
    }
}
