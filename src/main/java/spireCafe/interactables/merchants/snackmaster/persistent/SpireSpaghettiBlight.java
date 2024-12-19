package spireCafe.interactables.merchants.snackmaster.persistent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCBlight;
import spireCafe.util.Wiz;

public class SpireSpaghettiBlight extends AbstractSCBlight {
    public static final String ID = Anniv7Mod.makeID(SpireSpaghettiBlight.class.getSimpleName());
    public static final int TURN_AMT = 8;

    private boolean usedThisCombat = false;

    public SpireSpaghettiBlight() {
        super(ID, "spireSpaghetti.png");
        setCounter(TURN_AMT);
    }

    @Override
    public void atBattleStart() {
        if(!usedUp) {
            counter--;

            if(counter <= 0) {
                usedUp();
            }
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if(!usedUp && !usedThisCombat&& AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty()) {
            flash();
            Wiz.att(new ExhaustAction(999, true, false, false));
            Wiz.atb(new SkipEnemiesTurnAction());
            usedThisCombat = true;
        }
    }

    @Override
    public void onVictory() {
        if(usedUp) {
            AbstractDungeon.effectList.add(new AbstractGameEffect() {
                @Override
                public void update() {
                    isDone = true;
                    Wiz.p().blights.remove(SpireSpaghettiBlight.this);
                }
                public void render(SpriteBatch spriteBatch) {}
                public void dispose() {}
            });
        } else {
            usedThisCombat = false;
        }
    }

    @Override
    public AbstractBlight makeCopy() {
        return new SpireSpaghettiBlight();
    }
}
