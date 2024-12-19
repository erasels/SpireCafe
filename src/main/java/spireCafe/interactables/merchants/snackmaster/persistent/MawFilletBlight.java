package spireCafe.interactables.merchants.snackmaster.persistent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCBlight;
import spireCafe.util.Wiz;

public class MawFilletBlight extends AbstractSCBlight {
    public static final String ID = Anniv7Mod.makeID(MawFilletBlight.class.getSimpleName());
    public static final int STR_AMT = 2;
    public static final int TURN_AMT = 5;

    public MawFilletBlight() {
        super(ID, "mawFillet.png");
        setCounter(TURN_AMT);
    }

    @Override
    public void atBattleStart() {
        if(!usedUp) {
            flash();
            Wiz.applyToSelf(new StrengthPower(Wiz.p(), STR_AMT));
            counter--;

            if(counter <= 0) {
                usedUp();
            }
        }
    }

    @Override
    public void onVictory() {
        if(usedUp) {
            AbstractDungeon.effectList.add(new AbstractGameEffect() {
                @Override
                public void update() {
                    isDone = true;
                    Wiz.p().blights.remove(MawFilletBlight.this);
                }
                public void render(SpriteBatch spriteBatch) {}
                public void dispose() {}
            });
        }
    }

    @Override
    public String getDescription() {
        return String.format(blightStrings.DESCRIPTION[0], TURN_AMT, STR_AMT);
    }

    @Override
    public AbstractBlight makeCopy() {
        return new MawFilletBlight();
    }
}
