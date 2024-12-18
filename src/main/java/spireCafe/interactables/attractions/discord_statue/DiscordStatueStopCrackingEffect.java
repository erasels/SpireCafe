package spireCafe.interactables.attractions.discord_statue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import basemod.animations.SpriterAnimation;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractNPC;

public class DiscordStatueStopCrackingEffect extends AbstractGameEffect {
    private AbstractNPC character;

    public DiscordStatueStopCrackingEffect(AbstractNPC character) {
        this.duration = 1.95f;
        this.character = character;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();

        if (this.duration < 0.0f) {
            character.animation = new SpriterAnimation(
                    Anniv7Mod.makeAttractionPath("discord_statue/DiscordStatueCracked.scml"));
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

}
