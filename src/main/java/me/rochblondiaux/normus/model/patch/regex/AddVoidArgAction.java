package me.rochblondiaux.normus.model.patch.regex;

import lombok.NonNull;
import me.rochblondiaux.normus.model.regex.NormusRegexes;

import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class AddVoidArgAction extends RegexAction {

    public AddVoidArgAction() {
        super(NormusRegexes.EMPTY_FUNCTION);
    }

    @Override
    public void apply(@NonNull StringBuilder builder, @NonNull Matcher matcher) {
        if (matcher.groupCount() >= 3)
            builder.insert(matcher.end(3) - 1, "void");
    }
}
