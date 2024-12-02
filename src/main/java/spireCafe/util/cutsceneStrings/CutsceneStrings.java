package spireCafe.util.cutsceneStrings;

import com.megacrit.cardcrawl.localization.LocalizedStrings;

public class CutsceneStrings {
    public String NAME;
    public String[] DESCRIPTIONS;
    public String[] OPTIONS;
    public String[] BLOCKING_TEXTS;

    public CutsceneStrings() {
    }

    public static CutsceneStrings getMockCutsceneString() {
        CutsceneStrings mockCutsceneStrings = new CutsceneStrings();
        mockCutsceneStrings.NAME = "[MISSING_NAME]";
        mockCutsceneStrings.DESCRIPTIONS = LocalizedStrings.createMockStringArray(12);
        mockCutsceneStrings.OPTIONS = LocalizedStrings.createMockStringArray(12);
        return mockCutsceneStrings;
    }
}
