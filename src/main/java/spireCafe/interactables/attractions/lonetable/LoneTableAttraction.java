package spireCafe.interactables.attractions.lonetable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractAttraction;
import spireCafe.util.TexLoader;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class LoneTableAttraction extends AbstractAttraction {
    private static int blockingTextIndex = 0;
    public static final String ID = LoneTableAttraction.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(makeID(ID));
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(makeID(LoneTableCutscene.class.getSimpleName()));


    public static CharacterStrings getCharacterStrings() {
        return characterStrings;
    }

    public LoneTableAttraction(float animationX, float animationY) {
        super(animationX, animationY, 250, 200);
        img = TexLoader.getTexture(Anniv7Mod.makeAttractionPath("lonetable/LoneTable.png"));
        authors = "Cany0udance";
        name = characterStrings.NAMES[0];
        this.cutscenePortrait = null;
    }

    public void setCutscenePortrait(String texture) {
        String resourcePath = String.format("MatchAndKeepGremlin/%s.png", texture);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath(resourcePath)));
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {
        if (cutscenePortrait != null) {
            simpleRenderCutscenePortrait(sb, 1600.0F, 175.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    public void onInteract() {
        if (alreadyPerformedTransaction) {
            if (blockingDialogueIndex < cutsceneStrings.BLOCKING_TEXTS.length - 1) {
                blockingDialogueIndex++;
            }
        }
        AbstractDungeon.topLevelEffectsQueue.add(new LoneTableCutscene(this));
    }
}