package me.rochblondiaux.normus.model.patch.regex;

import lombok.NonNull;
import me.rochblondiaux.normus.model.regex.NormusRegexes;

import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class GlobalRenameAction extends RegexAction {

    public GlobalRenameAction() {
        super(NormusRegexes.VARIABLES);
    }

    @Override
    public void apply(@NonNull StringBuilder builder, @NonNull Matcher matcher) {
        if (matcher.groupCount() > 0)
            builder.replace(matcher.start(1), matcher.end(1), "g_" + matcher.group(1));
    }
}
