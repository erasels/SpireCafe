package spireCafe.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireCafe.Anniv7Mod;

public class RetainMod extends basemod.cardmods.RetainMod {
  public static String ID = Anniv7Mod.makeID(RetainMod.class.getSimpleName());
  private final boolean removeOnCardPlayed;

  public RetainMod() {
    this(false);
  }

  public RetainMod(boolean removeOnCardPlayed) {
    this.removeOnCardPlayed = removeOnCardPlayed;
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new RetainMod(this.removeOnCardPlayed);
  }

  @Override
  public boolean removeOnCardPlayed(AbstractCard card) {
    return super.removeOnCardPlayed(card) || this.removeOnCardPlayed;
  }
}
