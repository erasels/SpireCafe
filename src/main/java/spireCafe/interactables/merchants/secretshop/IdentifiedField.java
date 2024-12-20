package spireCafe.interactables.merchants.secretshop;

import java.util.EnumSet;

public enum IdentifiedField {
    COST, TYPE, RARITY;

    public static final EnumSet<IdentifiedField> FULLY_IDENTIFIED = EnumSet.allOf(IdentifiedField.class);
}
