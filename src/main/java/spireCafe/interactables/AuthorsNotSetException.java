package spireCafe.interactables;

import spireCafe.abstracts.AbstractCafeInteractable;

public class AuthorsNotSetException extends RuntimeException {
 public AuthorsNotSetException(Class<? extends AbstractCafeInteractable> interactableClz) {
     super("No authors set for this interactable: " + interactableClz.getSimpleName());
 }
}
