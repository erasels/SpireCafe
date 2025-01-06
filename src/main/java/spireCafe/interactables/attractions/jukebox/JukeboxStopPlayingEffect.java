package spireCafe.interactables.attractions.jukebox;

import basemod.animations.SpriterAnimation;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractNPC;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class JukeboxStopPlayingEffect extends AbstractGameEffect {
    private final AbstractNPC character;

    public JukeboxStopPlayingEffect(AbstractNPC character) {
        this.character = character;
        this.startingDuration = 2.0F; // Duration of the effect
        this.duration = this.startingDuration;
    }

    @Override
    public void update() {
        this.duration -= com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        if (this.duration <= 0.0F) {
            // Reset the jukebox animation to idle
            character.animation = new SpriterAnimation(
                    Anniv7Mod.makeAttractionPath("jukebox/JukeboxIdle.scml"));
            this.isDone = true;
        }
    }

    @Override
    public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch sb) {
        // No rendering needed for this effect
    }

    @Override
    public void dispose() {
        // Clean up resources if needed
    }
}
