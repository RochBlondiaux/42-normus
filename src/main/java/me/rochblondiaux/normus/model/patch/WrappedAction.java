package me.rochblondiaux.normus.model.patch;

import lombok.NonNull;
import me.rochblondiaux.normus.model.action.ActionConsumer;
import me.rochblondiaux.normus.model.context.ErrorContext;

import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public abstract class WrappedAction implements ActionConsumer {

    @Override
    public void patch(@NonNull ErrorContext context) {
        StringBuilder builder = new StringBuilder(context.getLine());
        apply(builder, context);
        context.getLines().set(context.getLineIndex(), builder.toString());
    }

    protected abstract void apply(@NonNull StringBuilder builder, @NonNull ErrorContext context);

}
