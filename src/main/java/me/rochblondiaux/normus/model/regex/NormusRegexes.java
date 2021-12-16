package me.rochblondiaux.normus.model.regex;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@AllArgsConstructor
@Getter
public enum NormusRegexes {
    FILE_HEADER(".*[.][a-zA-Z0-9]*"),
    FILE_ERROR_HEADER(".*[.][a-zA-Z0-9]*: Error!"),
    FILE_OK_HEADER(".*[.][a-zA-Z0-9]*: OK!"),
    BRACE("[\\s+]*[{]"),
    FUNCTION("^(\\w+(\\s+)?){2,}\\([^!@#$+%^]+?\\)"),
    SPACES("[\\s+]"),
    WHITE_SPACES("[ ]+"),
    RETURN("(\\w+(\\s+)?){2,}[;]"),
    TYPE_DEF("(})(\\s+)(\\w+(\\s+)?)(;)"),
    VARIABLES("([(*)(\\w+)(*)]((\\s+))?){1,}(;)"),
    ;

    private final String regex;

    public Pattern getPattern() {
        return Pattern.compile(regex);
    }

    public Matcher getMatcher(@NonNull String content) {
        return getPattern().matcher(content);
    }
}
