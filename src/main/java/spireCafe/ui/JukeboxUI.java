package spireCafe.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;

public class JukeboxUI {
    // Add attributes for song list, playback controls, etc.

    public JukeboxUI() {
        // Initialize UI components
    }

    public void update() {
        // Handle input and update UI components
    }

    public void render(SpriteBatch sb) {
        // Render the UI
        FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont, "Jukebox Screen", 400f, 700f, Color.WHITE);
        // Add more UI rendering as needed
    }
}
