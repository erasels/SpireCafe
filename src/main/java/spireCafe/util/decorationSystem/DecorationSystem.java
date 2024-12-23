package spireCafe.util.decorationSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.random.Random;
import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.util.TexLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DecorationSystem {
    private static final int NUM_DECOS = 2;
    private static final int PADDING = (int) (10 * Settings.scale);
    private static final float START_X = 15f * Settings.scale, CUTOFF_X = Settings.WIDTH - START_X;
    private static final float START_Y = 630f * Settings.scale, CUTOFF_Y = 870f * Settings.scale;
    private static List<Decoration> allDecorations;

    private List<Decoration> decorations;
    private Random rng;

    public DecorationSystem() {
        if (allDecorations == null) {
            initAllDecorations();
        }
        decorations = new ArrayList<>();
        rng = new Random(AbstractDungeon.miscRng.randomLong());
        spawnDecorations();
    }

    private void spawnDecorations() {
        Collections.shuffle(allDecorations, new java.util.Random(rng.randomLong()));

        decorations.add(new BarSignDecoration());
        initWindows();
        boolean hasLarge = false;
        int decorationAmt = NUM_DECOS;
        for (int i = 0; i < decorationAmt; i++) {
            Decoration d = allDecorations.get(i);
            initDeco(d);

            // Spawn an extra decoration if only 2 small ones were spawned
            if (d.isLarge) hasLarge = true;
            if (!hasLarge && i + 1 == NUM_DECOS) {
                decorationAmt++;
            }
        }
    }

    private void initDeco(Decoration deco) {
        float x, y;

        // Try up to 20 times to find a valid position for the decoration
        for (int attempts = 0; attempts < 20; attempts++) {
            // Generate a random position within the valid range
            if(!deco.isXFixed()) {
                x = rng.random(START_X, CUTOFF_X - deco.width);
            } else {
                x = deco.fixedX;
            }
            if(!deco.isYFixed()) {
                y = rng.random(START_Y, CUTOFF_Y - deco.height);
            } else {
                y = deco.fixedY;
            }


            // Check if the position is valid (doesn't overlap and within bounds)
            if (canPlaceDecoration(deco, x, y)) {
                deco.move(x, y); // Place the decoration
                decorations.add(deco);
                break;
            }
        }
    }

    private boolean canPlaceDecoration(Decoration deco, float x, float y) {
        for (Decoration existing : decorations) {
            if (overlaps(existing, deco, x, y)) {
                return false;
            }
        }
        return true;
    }

    private boolean overlaps(Decoration existing, Decoration deco, float x, float y) {
        float ex = existing.x, ey = existing.y;
        float ew = existing.width + PADDING, eh = existing.height + PADDING;

        return !(x + deco.width < ex || x > ex + ew || y + deco.height < ey || y > ey + eh);
    }

    private void initWindows() {
        float x1 = 490f * Settings.scale;
        float x2 = 1430f * Settings.scale;
        float maxWiggle = 50f * Settings.scale;

        // Figure out which prefix to use based on dark vs. light background
        boolean darkBg = ((CafeRoom) AbstractDungeon.getCurrRoom().event).darkBg;
        String noLights  = darkBg ? "window_dark_noLights"  : "window_light_noLights";
        String lights    = darkBg ? "window_dark_lights"    : "window_light_lights";

        // Randomly decide which one goes first vs. second
        boolean firstIsNoLights = rng.randomBoolean();
        String firstWindow  = firstIsNoLights ? noLights : lights;
        String secondWindow = firstIsNoLights ? lights   : noLights;

        // Always place the first window
        WindowDecoration w = new WindowDecoration(firstWindow);
        w.move(x1 + rng.random(-maxWiggle, maxWiggle), w.fixedY);
        decorations.add(w);

        // Optionally place the second window
        if (rng.randomBoolean()) {
            w = new WindowDecoration(secondWindow);
            w.move(x2 + rng.random(-maxWiggle, maxWiggle), w.fixedY);
            decorations.add(w);
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        decorations.forEach(d -> d.render(sb));
    }

    public void update() {
        decorations.forEach(Decoration::update);
    }

    public void dispose() {
        decorations.clear();
    }

    public void redecorate() {
        decorations.clear();
        spawnDecorations();
    }

    private static void initAllDecorations() {
        allDecorations = new ArrayList<>();

        allDecorations.add(new Decoration("bold_and_brash", TexLoader.getTexture(Anniv7Mod.makeUIPath("decoration/bold_and_brash.png")), 0, 0));
        allDecorations.add(new Decoration("cultist_portrait", TexLoader.getTexture(Anniv7Mod.makeUIPath("decoration/cultist_portrait.png")), 0, 0));
        allDecorations.add(new Decoration("framed_louse", TexLoader.getTexture(Anniv7Mod.makeUIPath("decoration/framed_louse.png")), 0, 0));
        allDecorations.add(new Decoration("poster", TexLoader.getTexture(Anniv7Mod.makeUIPath("decoration/poster.png")), 0, 0));
        allDecorations.add(new Decoration("potted_plant", TexLoader.getTexture(Anniv7Mod.makeUIPath("decoration/potted_plant.png")), 0, 0));
        allDecorations.add(new Decoration("spire_cafe_sign", TexLoader.getTexture(Anniv7Mod.makeUIPath("decoration/spire_cafe_sign.png")), 0, 0));
        allDecorations.add(new StackedShelfDecoration());
    }
}
