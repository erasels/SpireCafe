package spireCafe.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.abstracts.AbstractAttraction;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.abstracts.AbstractCafeInteractable.FacingDirection;

// !!--- READ BEFORE EDITING ---!!
//
// The methods in this class exist purely to allow other mods to easily get
// information about the Cafe using reflection (thus why they mostly operate
// on Class objects instead of the types themselves).
// This was originally built for a Spire Biomes crossover, but can be used by
// other mods. Effectively, this is a public API for the Cafe, and it should
// not be changed since doing so could break any mods using it.

public class CafeUtil {

    public static HashMap<Class<? extends AbstractCafeInteractable>, Method> canSpawnMethods = new HashMap<>();

    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getAllInteractableByType(Class<? extends AbstractCafeInteractable> clz) {
        return Anniv7Mod.interactableClasses.entrySet().stream()
        .filter(e -> clz.isAssignableFrom(e.getValue()))
        .collect(Collectors.toMap(
            e -> e.getKey(),
            e -> e.getValue(),
            (oldValue, newValue) -> oldValue,
            HashMap::new));
    }
    
    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getAllAttractions() {
         return getAllInteractableByType(AbstractAttraction.class);
    }
    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getAllBartenders() {
         return getAllInteractableByType(AbstractBartender.class);
    }
    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getAllMerchants() {
         return getAllInteractableByType(AbstractMerchant.class);
    }
    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getAllPatrons() {
         return getAllInteractableByType(AbstractPatron.class);
    }

    private static boolean canSpawn(Class<? extends AbstractCafeInteractable> clz) {
        return CafeRoom.canSpawn(clz);
    }
    
    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getAllValidInteractableByType(Class<? extends AbstractCafeInteractable> clz) {
        return CafeRoom.getPossibilitiesStream(clz)
        .collect(Collectors.toMap(
            e -> e.getKey(),
            e -> e.getValue(),
            (oldValue, newValue) -> oldValue,
            HashMap::new));
    }

    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getValidAttractions() {
         return getAllValidInteractableByType(AbstractAttraction.class);
    }
    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getValidBartenders() {
         return getAllValidInteractableByType(AbstractBartender.class);
    }
    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getValidMerchants() {
         return getAllValidInteractableByType(AbstractMerchant.class);
    }
    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getValidPatrons() {
         return getAllValidInteractableByType(AbstractPatron.class);
    }

    public static List<String> getValidAttractionIDs() {
        return getValidAttractions().keySet().stream().collect(Collectors.toList());
    }
    public static List<String> getValidBartenderIDs() {
        return getValidBartenders().keySet().stream().collect(Collectors.toList());
    }
    public static List<String> getValidMerchantIDs() {
        return getValidMerchants().keySet().stream().collect(Collectors.toList());
    }
    public static List<String> getValidPatronIDs() {
        return getValidPatrons().keySet().stream().collect(Collectors.toList());
    }

    public static void markInteractableAsSeen(String ID){
        Anniv7Mod.currentRunSeenInteractables.add(ID);
    }

    public static void markInteractableAsSeen(AbstractCafeInteractable interactable) {
        Anniv7Mod.currentRunSeenInteractables.add(interactable.id);
    }

    public static void clearRunSeenInteractables() {
        Anniv7Mod.currentRunSeenInteractables.clear();
    }

    public static Class <? extends AbstractCafeInteractable> getInteractableClass(String ID) {
        return Anniv7Mod.interactableClasses.get(ID);
    }

    public static AbstractCafeInteractable createInteractable(Class<? extends AbstractCafeInteractable> clz, float x, float y) {
        return CafeRoom.createInteractable(clz, x, y);
    }

    public static AbstractCafeInteractable createInteractable(String ID, float x, float y) {
        return createInteractable(Anniv7Mod.interactableClasses.get(ID), x, y);
    }

    public static void makeFaceRight(AbstractCafeInteractable interactable) {
        if (interactable.facingDirection == FacingDirection.LEFT) {
            interactable.facingDirection = FacingDirection.RIGHT;
            interactable.flipHorizontal = !interactable.flipHorizontal;
        }
    }
    public static void makeFaceLeft(AbstractCafeInteractable interactable) {
        if (interactable.facingDirection == FacingDirection.RIGHT) {
            interactable.facingDirection = FacingDirection.LEFT;
            interactable.flipHorizontal = !interactable.flipHorizontal;
        }
    }

}
