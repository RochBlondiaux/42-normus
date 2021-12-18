package me.rochblondiaux.normus.model.patch.regex;

import lombok.NonNull;
import me.rochblondiaux.normus.model.regex.NormusRegexes;

import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class BraceNewLineAction extends RegexAction {

    public BraceNewLineAction() {
        super(NormusRegexes.BRACE);
    }

    @Override
    public void apply(@NonNull StringBuilder builder, @NonNull Matcher matcher) {
        builder.replace(matcher.start(), matcher.end(), "\n{");
    }
}
