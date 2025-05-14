package spireCafe.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractAttraction;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.abstracts.AbstractCafeInteractable.FacingDirection;

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
        Method canSpawnMethod = null;
        if (canSpawnMethods.containsKey(clz)) {
            canSpawnMethod = canSpawnMethods.get(clz);
        }
        else {
            Method[] methods = clz.getDeclaredMethods();
            for (Method m : methods) {
                if (Modifier.isStatic(m.getModifiers()) && m.getName().equals("canSpawn") && m.getReturnType().equals(boolean.class) && m.getParameterCount() == 0) {
                    m.setAccessible(true);
                    canSpawnMethod = m;
                    break;
                }
            }
            canSpawnMethods.put(clz, canSpawnMethod);
        }
        try {
            return canSpawnMethod == null || (boolean)canSpawnMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static HashMap<String, Class<? extends AbstractCafeInteractable>> getAllValidInteractableByType(Class<? extends AbstractCafeInteractable> clz) {
        return Anniv7Mod.interactableClasses.entrySet().stream()
        .filter(e -> clz.isAssignableFrom(e.getValue()))
        .filter(e -> !Anniv7Mod.currentRunSeenInteractables.contains(e.getKey()))
        .filter(e -> canSpawn(e.getValue()))
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

    public static Class <? extends AbstractCafeInteractable> getInteractableClass(String ID) {
        return Anniv7Mod.interactableClasses.get(ID);
    }

    public static AbstractCafeInteractable createInteractable(Class<? extends AbstractCafeInteractable> clz, float x, float y) {
        try {
            return clz.getConstructor(float.class, float.class).newInstance(x, y);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Error creating interactable " + clz.getName(), e);
        }
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
