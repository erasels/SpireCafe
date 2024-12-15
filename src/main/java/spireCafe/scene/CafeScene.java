package spireCafe.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.scenes.AbstractScene;

public class CafeScene extends AbstractScene {
    public CafeScene(){
        super("bottomScene/scene.atlas");
        this.ambianceName = "AMBIANCE_BOTTOM";
    }

    @Override
    public void renderCombatRoomBg(SpriteBatch spriteBatch) {

    }

    @Override
    public void renderCombatRoomFg(SpriteBatch spriteBatch) {

    }

    @Override
    public void renderCampfireRoom(SpriteBatch spriteBatch) {

    }

    @Override
    public void randomizeScene() {

    }
}
