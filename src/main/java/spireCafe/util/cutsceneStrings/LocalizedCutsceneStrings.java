package spireCafe.util.cutsceneStrings;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LocalizedCutsceneStrings {
    private static final String LOCALIZATION_DIR = "localization";
    private static Map<String, CutsceneStrings> cutscenes = new HashMap<String, CutsceneStrings>();
    private static Gson gson = new Gson();
    private static Type cutsceneType = (new TypeToken<Map<String, CutsceneStrings>>() {
    }).getType();

    public static void loadCutsceneStrings(String jsonString) {
        loadJsonStrings(jsonString);
    }

    public static void loadCutsceneStringsFile(String filepath) {
        loadJsonStrings(Gdx.files.internal(filepath).readString(String.valueOf(StandardCharsets.UTF_8)));
    }

    private static void loadJsonStrings(String jsonString){
        Map map = new HashMap((Map)gson.fromJson(jsonString, cutsceneType));
        cutscenes.putAll(map);
    }

    public static CutsceneStrings getCutsceneStrings(String cutsceneName) {
        if (cutscenes.containsKey(cutsceneName)) {
            return  cutscenes.get(cutsceneName);
        } else {
            return CutsceneStrings.getMockCutsceneString();
        }
    }
}
