package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static spireCafe.Anniv7Mod.modID;

public class MarkovChain {
    private static final Map<MarkovType, MarkovChain> instances = new HashMap<>();
    private final Map<String, List<String>> markovChain = new HashMap<>();
    private final Random random = new Random();

    private MarkovChain(MarkovType type) {
        FileHandle fileHandle;
        switch(type) {
            case CARD:
                fileHandle = Gdx.files.internal(modID + "Resources/localization/eng/MissingnoPatron/markov-cards.txt");
                break;
            case RELIC:
                fileHandle = Gdx.files.internal(modID + "Resources/localization/eng/MissingnoPatron/markov-relics.txt");
                break;
            case FLAVOR:
                fileHandle = Gdx.files.internal(modID + "Resources/localization/eng/MissingnoPatron/markov-relic-flavors.txt");
                break;
            case PAGE:
                fileHandle = Gdx.files.internal(modID + "Resources/localization/eng/MissingnoPatron/markov-pages.txt");
                break;
            default:
                fileHandle = Gdx.files.internal(modID + "Resources/localization/eng/MissingnoPatron/markov-text.txt");
        }
        String text = fileHandle.readString(String.valueOf(StandardCharsets.UTF_8));
        buildChain(text);
    }

    public static MarkovChain getInstance(MarkovType type) {
        return instances.computeIfAbsent(type, MarkovChain::new);
    }

    public void buildChain(String text) {
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length - 1; i++) {
            String word = words[i];
            String nextWord = words[i + 1];
            markovChain.computeIfAbsent(word, k -> new ArrayList<>()).add(nextWord);
        }
    }

    private String getRandomSeed() {
        List<String> keys = new ArrayList<>(markovChain.keySet());
        if (keys.isEmpty()) {
            return "";
        }
        return keys.get(random.nextInt(keys.size()));
    }

    public String generateText() {
        return generateText(25, 40);
    }

    public String generateText(int startRange, int endRange) {
        String resultText = "";
        int wordCount = 0;

        while (wordCount < 3) {
            String seed = getRandomSeed();
            int length = random.nextInt((endRange - startRange) + 1) + startRange;
            StringBuilder result = new StringBuilder(seed);
            String currentWord = seed.toLowerCase();

            for (int i = 0; i < length; i++) {
                List<String> nextWords = markovChain.get(currentWord);
                if (nextWords == null || nextWords.isEmpty()) {
                    break;
                }
                String nextWord = nextWords.get(random.nextInt(nextWords.size()));
                result.append(" ").append(nextWord);
                currentWord = nextWord;
            }

            resultText = result.toString();
            wordCount = resultText.split("\\s+").length;
        }

        return resultText;
    }


    public enum MarkovType {
        MISSINGNO,
        CARD,
        RELIC,
        FLAVOR,
        PAGE
    }
}