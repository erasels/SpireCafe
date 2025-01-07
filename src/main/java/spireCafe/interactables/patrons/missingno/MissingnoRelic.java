package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.PutOnDeckAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenEye;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireCafe.abstracts.AbstractSCRelic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.miscRng;
import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.missingno.MarkovChain.MarkovType.FLAVOR;
import static spireCafe.interactables.patrons.missingno.MarkovChain.MarkovType.RELIC;
import static spireCafe.interactables.patrons.missingno.MissingnoPatron.assetID;
import static spireCafe.util.Wiz.atb;

public class MissingnoRelic extends AbstractSCRelic {

    public static final String ID = makeID(MissingnoRelic.class.getSimpleName());
    private int threshold = 10;

    public MissingnoRelic() {
        super(ID, assetID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.counter = 0;
        flavorText = MarkovChain.getInstance(FLAVOR).generateText(5, 15);
    }

    @Override
    public String getUpdatedDescription() {
        return MarkovChain.getInstance(RELIC).generateText(5, 15);
    }

    @Override
    public boolean checkTrigger() {
        counter++;
        flash();
        if(counter == threshold) {
            flash();
            atb(new SFXAction("BUFF_1"));
            counter = 0;
            threshold = miscRng.random(10);
            return true;
        }
        return false;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if(miscRng.random(100) < 3 ) {
            checkTrigger();
        }
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        if(miscRng.random(100) < 10 ) {
            checkTrigger();
        }
    }

    @Override
    public void atBattleStart() {
        if(miscRng.random(100) < 5 ) {
            checkTrigger();

        }
    }

    @Override
    public void atTurnStartPostDraw() {
        if(miscRng.random(100) < 3 ) {
            checkTrigger();
        }
    }

    @Override
    public void atTurnStart() {
        if(miscRng.random(100) < 10 ) {
            AbstractDungeon.player.drawPile.addToRandomSpot(new MissingnoCard());
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if(miscRng.random(100) < 3 ) {
            checkTrigger();
        }
    }

    @Override
    public void onManualDiscard() {
        if(miscRng.random(100) < 10 ) {
            checkTrigger();
        }
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(miscRng.random(100) < 1 ) {
            checkTrigger();
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if(miscRng.random(100) < 3 ) {
            checkTrigger();
        }
    }

    @Override
    public void onRest() {
        if(miscRng.random(100) < 10 ) {
            checkTrigger();
        }
    }

    @Override
    public void onShuffle() {
        if(miscRng.random(100) < 10 ) {
            checkTrigger();
        }
    }

    @Override
    public void onSmith() {
        if(miscRng.random(100) < 15 ) {
            checkTrigger();
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if(miscRng.random(100) < 5 ) {
            checkTrigger();
        }
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(miscRng.random(100) < 5 ) {
            checkTrigger();
        }
    }

    @Override
    public void onVictory() {
        MissingnoPatches.GlitchedPlayerFields.glitchOffset.set(AbstractDungeon.player, 200);
    }
}
