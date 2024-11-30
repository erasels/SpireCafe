package spireCafe.interactables.merchants.example;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;

public class ExampleMerchant extends AbstractMerchant {

    public ExampleMerchant(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = "TEMPDOSTRINGS";
        this.img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("ExampleNPC/image.png"));
    }

    @Override
    public void onInteract() {

    }

}
