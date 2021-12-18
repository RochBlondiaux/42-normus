package me.rochblondiaux.normus.model.patch.regex;

import lombok.Data;
import lombok.NonNull;
import me.rochblondiaux.normus.model.action.ActionConsumer;
import me.rochblondiaux.normus.model.context.ErrorContext;
import me.rochblondiaux.normus.model.regex.NormusRegexes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
public abstract class RegexAction implements ActionConsumer {

    private final NormusRegexes regex;

    @Override
    public void patch(@NonNull ErrorContext context) {
        StringBuilder builder = new StringBuilder(context.getLine());
        Matcher matcher = regex.getMatcher(context.getLine());
        if (matcher.find())
            apply(builder, matcher);
        context.getLines().set(context.getLineIndex(), builder.toString());
    }

    public abstract void apply(@NonNull StringBuilder builder, @NonNull Matcher matcher);
}
