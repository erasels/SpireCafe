package spireCafe.interactables.patrons.powerelic.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireCafe.interactables.patrons.powerelic.PowerelicCutscene;
import spireCafe.util.Wiz;

import java.sql.Array;
import java.util.ArrayList;

public class PowerelicDebugConversionEffect extends AbstractGameEffect {

    ArrayList<AbstractCard> powers=new ArrayList<>();
    int relicsToConvert=-1;
    public PowerelicDebugConversionEffect(ArrayList<AbstractCard> powers,int relicsToConvert){
        this();
        this.powers=powers;
        this.relicsToConvert=relicsToConvert;
    }
    public PowerelicDebugConversionEffect(){
        super();
        this.color=Color.GREEN;
    }
    @Override
    public void render(SpriteBatch spriteBatch) {}

    public void update(){
        super.update();
        boolean powersWasEmpty=(powers.isEmpty()); //convertSelectedCards also clears the selected list
        PowerelicCutscene.convertSelectedCardsToRelics(powers, (relicsToConvert>=0));
        if(relicsToConvert>=0 && !powersWasEmpty){
            PowerelicCutscene.convertRandomRelicsToPowers(relicsToConvert);
        }
        isDone=true;
    }

    @Override
    public void dispose() {}
}
