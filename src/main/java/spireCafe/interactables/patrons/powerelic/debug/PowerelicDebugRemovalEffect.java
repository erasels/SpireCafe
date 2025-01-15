package spireCafe.interactables.patrons.powerelic.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireCafe.util.Wiz;

public class PowerelicDebugRemovalEffect extends AbstractGameEffect {

    public PowerelicDebugRemovalEffect(){
        super();
        this.color=Color.GREEN;
    }
    @Override
    public void render(SpriteBatch spriteBatch) {}

    public void update(){
        super.update();
        Wiz.adp().loseRelic(PowerelicDebugRelic.ID);
        isDone=true;
    }

    @Override
    public void dispose() {}
}
