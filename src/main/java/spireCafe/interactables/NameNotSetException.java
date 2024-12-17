package spireCafe.interactables;

import spireCafe.abstracts.AbstractCafeInteractable;

public class NameNotSetException extends RuntimeException {
 public NameNotSetException(Class<? extends AbstractCafeInteractable> interactableClz) {
     super("No name set for this interactable: " + interactableClz.getSimpleName());
 }
}
