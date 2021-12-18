package me.rochblondiaux.normus.model.patch.regex;

import lombok.NonNull;
import me.rochblondiaux.normus.model.regex.NormusRegexes;

import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class AddSpaceParenthesisAction extends RegexAction {

    public AddSpaceParenthesisAction() {
        super(NormusRegexes.PARENTHESIS_BRACES);
    }

    @Override
    public void apply(@NonNull StringBuilder builder, @NonNull Matcher matcher) {
        if (matcher.groupCount() >= 2)
            builder.insert(matcher.end(2) - 1, ' ');
    }
}
