package me.rochblondiaux.normus;

import me.rochblondiaux.normus.model.regex.NormusRegexes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class SpaceReplaceTabTest {

    String apply(String a, int i) {
        Matcher matcher = NormusRegexes.WHITE_SPACES.getMatcher(a);
        if (!matcher.find())
            return a;
        return matcher.replaceAll(matchResult -> {
            double tabs = matchResult.group().length() / 4d;
            if (tabs == 0)
                tabs += 1;
            return "\t".repeat((int) Math.ceil(tabs));
        });
    }

    @DisplayName("Simple case")
    @Test
    void testSimpleCase() {
        String test = "    int a;";
        String after = apply(test, 5);
        after = apply(after, 5);
        assert after.equals("\tint\ta;");
    }

    @DisplayName("Common case")
    @Test
    void testCommonCase() {
        String test = "      int  a;";
        String after = apply(test, 5);
        after = apply(after, 7);
        assert after.equals("\t\tint\ta;");
    }
}
