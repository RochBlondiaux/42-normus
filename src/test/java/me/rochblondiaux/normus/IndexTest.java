package me.rochblondiaux.normus;

import me.rochblondiaux.normus.utils.ParsingUtils;
import org.junit.jupiter.api.Test;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class IndexTest {

    @Test
    void basicTest() {
        String line = "\tint\ta;";
        assert line.charAt(ParsingUtils.getRealIndex(line, 4)) == 'i';
        assert line.charAt(ParsingUtils.getRealIndex(line, 5)) == 'n';
        assert line.charAt(ParsingUtils.getRealIndex(line, 11)) == 'a';
    }
}
