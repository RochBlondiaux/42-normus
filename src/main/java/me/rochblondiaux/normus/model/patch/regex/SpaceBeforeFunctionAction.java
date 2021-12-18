package me.rochblondiaux.normus.model.patch.regex;

import lombok.NonNull;
import me.rochblondiaux.normus.model.regex.NormusRegexes;

import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class SpaceBeforeFunctionAction extends RegexAction {

    public SpaceBeforeFunctionAction() {
        super(NormusRegexes.FUNCTION);
    }

    @Override
    public void apply(@NonNull StringBuilder builder, @NonNull Matcher matcher) {
        if (matcher.groupCount() >= 2)
            builder.replace(matcher.start(2), matcher.end(2), "\t");
    }
}
