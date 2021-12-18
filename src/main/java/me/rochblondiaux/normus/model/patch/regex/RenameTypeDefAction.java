package me.rochblondiaux.normus.model.patch.regex;

import lombok.NonNull;
import me.rochblondiaux.normus.model.regex.NormusRegexes;

import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class RenameTypeDefAction extends RegexAction {

    public RenameTypeDefAction() {
        super(NormusRegexes.TYPE_DEF);
    }

    @Override
    public void apply(@NonNull StringBuilder builder, @NonNull Matcher matcher) {
        if (matcher.groupCount() >= 5)
            builder.replace(matcher.start(5), matcher.end(5), matcher.group().toUpperCase());
    }
}
