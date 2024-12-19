package spireCafe.interactables.attractions.discord_statue.modifiers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.abstracts.AbstractCardModifier;

public class AnagramNameModifier extends AbstractCardModifier {
    private String name;

    public AnagramNameModifier(String name) {
        this.name = name;
    }

    public AnagramNameModifier() {
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        while (this.name == null || this.name.equals(card.name)) {
            this.name = createAnagram(card.name);
        }
    }

    private String createAnagram(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters);
        StringBuilder result = new StringBuilder(characters.size());
        for (char c : characters) {
            result.append(c);
        }
        return result.toString();
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return card.upgraded ? name + "+" : name;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AnagramNameModifier(name);
    }

}
