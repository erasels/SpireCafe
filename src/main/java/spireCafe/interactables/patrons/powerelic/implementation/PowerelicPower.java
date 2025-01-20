package spireCafe.interactables.patrons.powerelic.implementation;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireCafe.abstracts.AbstractSCPower;
import spireCafe.util.Wiz;

import java.util.ArrayList;
import java.util.Iterator;

public class PowerelicPower extends AbstractSCPower {

    public static final String POWER_ID = PowerelicPatron.class.getSimpleName();
    public AbstractRelic sourceRelic;
    private static int powerIdOffset;

    public PowerelicPower(String ID, String NAME, PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
        super(ID, NAME, powerType, isTurnBased, owner, amount);
    }
    public PowerelicPower(AbstractRelic sourceRelic){
        this(PowerelicPower.POWER_ID,sourceRelic.name,PowerType.BUFF,false, Wiz.adp(),sourceRelic.counter);
        this.sourceRelic=sourceRelic;
        this.ID = "PowerelicPower" + powerIdOffset;
        ++powerIdOffset;
    }

    public void update(int slot){
        if(sourceRelic!=null) {
            this.name = sourceRelic.name;
            this.description = sourceRelic.description;
            this.amount = sourceRelic.counter;
            this.img = sourceRelic.img;
            if(this.img!=null)this.region48=null;

        }
        super.update(slot);
    }

    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        //relic: sb.draw(this.img, this.currentX - 64.0F, this.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, this.rotation, 0, 0, 128, 128, false, false);
        if (this.img != null) {
            sb.setColor(c);
            sb.draw(this.img, x - 44F*Settings.scale, y - 52F*Settings.scale, 0F, 0F, 128F, 128F, 1.25F*48/128F*Settings.scale * 1.5F, 1.5F*48/128F*Settings.scale * 1.5F, 0.0F, 0, 0, 128, 128, false, false);
        }
//        } else {
//            sb.setColor(c);
//
//            if (Settings.isMobile) {
//                sb.draw(this.region48, x - (float)this.region48.packedWidth / 2.0F, y - (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth / 2.0F, (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth, (float)this.region48.packedHeight, Settings.scale * 1.17F, Settings.scale * 1.17F, 0.0F);
//            } else {
//                sb.draw(this.region48, x - (float)this.region48.packedWidth / 2.0F, y - (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth / 2.0F, (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth, (float)this.region48.packedHeight, Settings.scale, Settings.scale, 0.0F);
//            }
//        }
        ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(this, AbstractPower.class,"effect");
        Iterator var5 = effect.iterator();
        while(var5.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)var5.next();
            e.render(sb, x, y);
        }
    }
    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb,x,y,c);
        if (this.amount == 0) {
            if (!this.isTurnBased) {
                Color greenColor=ReflectionHacks.getPrivate(this,AbstractPower.class,"greenColor");
                greenColor.a = c.a;
                c = greenColor;
            }
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        }
    }


}
