package spireCafe.interactables.patrons.trashking;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;

import java.util.Arrays;

public class TrashKingPatron extends AbstractPatron {
    public static final String ID = TrashKingPatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));

    public static final String assetID = "TrashKing";

    public TrashKingPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Cany0udance, chocolatecake5000";

        img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("TrashKing/Portrait.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("TrashKing/Portrait2.png")));
    }

    public void setCutscenePortrait(String texture) {
        String resourcePath = String.format("TrashKing/%s.png", texture);
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath(resourcePath)));
    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1650.0F, 50.0F, 0.0F, 0.0F, 0.0F);
    }

    public static boolean canSpawn() {
        return Arrays.stream(TrashKingCutscene.TRASH_KING_RELICS)
                .map(s -> !AbstractDungeon.player.hasRelic(s))
                .count() > 1;
    }

    @Override
    public void onInteract() {
        if (canSpawn()) {
            AbstractDungeon.topLevelEffectsQueue.add(new TrashKingCutscene(this));
        }
    }
}