package me.rochblondiaux.normus.model.patch.misc;

import lombok.NonNull;
import me.rochblondiaux.normus.model.context.ErrorContext;
import me.rochblondiaux.normus.model.patch.WrappedAction;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class LowercaseAction extends WrappedAction {

    @Override
    protected void apply(@NonNull StringBuilder builder, @NonNull ErrorContext context) {
        for (int i = 0; i < builder.length(); i++) {
            if (Character.isUpperCase(builder.charAt(i))) {
                builder.setCharAt(i, Character.toLowerCase(builder.charAt(i)));
                builder.insert(i, '_');
                break;
            }
        }
    }
}
