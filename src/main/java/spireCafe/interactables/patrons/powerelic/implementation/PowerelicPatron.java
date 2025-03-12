package spireCafe.interactables.patrons.powerelic.implementation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.interactables.patrons.powerelic.PowerelicAllowlist;
import spireCafe.util.TexLoader;

import java.util.ArrayList;

public class PowerelicPatron extends AbstractPatron {
    public static final String ID = PowerelicPatron.class.getSimpleName();
    public static final String assetID = "PowerelicPatron";
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    protected boolean alreadyTalkedOnce=false;
    public boolean alreadyShowedBlockingDialogue=false;
    public boolean alreadyBoopedSnoot=false;
    protected boolean ignoreLooter=false;
    public Hitbox snootHitbox;
    public TextureRegion standardCutscenePortrait;

    public PowerelicPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.authors = "Lua Viper";
        this.animationY=this.animationY-20f;

        float snoot_x_offset=-10;
        float snoot_y_offset=140;
        float snoot_w=15;
        float snoot_h=15;
        snootHitbox=new Hitbox(this.animationX + (snoot_x_offset * Settings.scale), this.animationY + (snoot_y_offset * Settings.scale), snoot_w * Settings.scale, snoot_h * Settings.scale);

        img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Powerelic/patron.png"));
        standardCutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Powerelic/portrait.png")));
        cutscenePortrait = standardCutscenePortrait;
    }

    public static boolean canSpawn() {
        return (!PowerelicAllowlist.getAllConvertibleRelics().isEmpty() && !getAllConvertiblePowers().isEmpty());
    }

    @Override
    public void renderCutscenePortrait(SpriteBatch sb) {
        if(cutscenePortrait!=null) {
            simpleRenderCutscenePortrait(sb, 1560.0F, -200.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        super.renderAnimation(sb);
        snootHitbox.render(sb);
    }
    public void update() {
        snootHitbox.update();
        super.update();
    }
    public void onInteract() {
        boolean snootBoop=false;
        if(snootHitbox.hovered && !AbstractDungeon.isScreenUp && !AbstractCutscene.isInCutscene){
            if(!alreadyBoopedSnoot) {
                //Anniv7Mod.logger.info("Powerelic: BOOP");
                alreadyBoopedSnoot = true;
                snootBoop=true;
            }
        }
        AbstractDungeon.topLevelEffectsQueue.add(new PowerelicCutscene(this,snootBoop));
        alreadyTalkedOnce=true;
    }

    public static ArrayList<AbstractCard> getAllConvertiblePowers(){
        ArrayList<AbstractCard> powers=AbstractDungeon.player.masterDeck.getPurgeableCards().getCardsOfType(AbstractCard.CardType.POWER).group;
        powers.removeIf(card -> (card instanceof PowerelicCard));
        return powers;
    }
}
