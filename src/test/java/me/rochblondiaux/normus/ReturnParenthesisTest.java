package me.rochblondiaux.normus;

import me.rochblondiaux.normus.model.regex.NormusRegexes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class ReturnParenthesisTest {

    String apply(String a) {
        a = a.replaceAll("\\s\\s+", " ");
        Matcher matcher = NormusRegexes.RETURN.getMatcher(a);
        if (!matcher.find() || matcher.groupCount() < 2)
            return a;
        return a.replace(matcher.group(1), '(' + matcher.group(1) + ')');
    }

    @Test
    @DisplayName("Basic test")
    void basicTest() {
        String test = "return a;";
        String excepted = "return (a);";
        assert excepted.equals(apply(test));
    }

    @Test
    @DisplayName("Common test")
    void commonTest() {
        String test = "return  \ta;";
        String excepted = "return (a);";
        assert excepted.equals(apply(test));
    }

    @Test
    @DisplayName("Complex test")
    void complexTest() {
        String test = "return \t\t\ta;";
        String excepted = "return (a);";
        assert excepted.equals(apply(test));
    }
}
