package spireCafe.interactables.patrons.cleric;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import basemod.helpers.CardModifierManager;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.cardmods.BlessedMod;
import spireCafe.util.Wiz;

public class CompassionMarkRelic extends AbstractSCRelic {

    private static final String ID = Anniv7Mod.makeID(CompassionMarkRelic.class.getSimpleName());

    public CompassionMarkRelic() {
        super(ID, "ClericPatron", RelicTier.SPECIAL, LandingSound.MAGICAL);
    }
    
    @Override
    public void onEquip() {
        super.onEquip();
        ArrayList<AbstractCard> miracleCards = new ArrayList<>();

        for (AbstractCard c : Wiz.deck().group) {
            if (c.cost > 0) {
                miracleCards.add(c);
            }
        }

        Collections.shuffle(miracleCards, new Random(AbstractDungeon.relicRng.randomLong()));

        for (int i = 0; i < 5; i++) {
            AbstractCard c = miracleCards.get(i);
            CardModifierManager.addModifier(c, new BlessedMod());
            showChangedCard(c);
        }

        CardCrawlGame.sound.play("HEAL_3");

    }

    private void showChangedCard(AbstractCard c) {
        float x = Settings.WIDTH * 0.5F + MathUtils.random.nextFloat() * Settings.WIDTH * 0.75F - Settings.WIDTH * 0.375F;
        float y = Settings.HEIGHT * 0.5F + MathUtils.random.nextFloat() * Settings.HEIGHT * 0.35F - Settings.HEIGHT * 0.175F;
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
    }

}
