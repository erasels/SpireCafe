package spireCafe.interactables.attractions.shrineoforder;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractAttraction;
import spireCafe.util.TexLoader;

public class ShrineOfOrderAttraction extends AbstractAttraction {
    public static final String ID = ShrineOfOrderAttraction.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack
            .getCharacterString(Anniv7Mod.makeID(ID));

    public ShrineOfOrderAttraction(float animationX, float animationY) {
        super(animationX, animationY, 200, 380);
        img = TexLoader.getTexture(Anniv7Mod.makeAttractionPath("shrineoforder/ShrineOfOrder.png"));
        authors = "DJM";
        name = characterStrings.NAMES[0];
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        super.renderAnimation(sb);
    }

    @Override
    public void onInteract() {
        if (!alreadyPerformedTransaction)
            AbstractDungeon.topLevelEffectsQueue.add(new ShrineOfOrderCutscene(this));
    }

}
