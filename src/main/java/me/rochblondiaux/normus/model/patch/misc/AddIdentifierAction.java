package me.rochblondiaux.normus.model.patch.misc;

import lombok.NonNull;
import me.rochblondiaux.normus.model.context.ErrorContext;
import me.rochblondiaux.normus.model.patch.WrappedAction;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class AddIdentifierAction extends WrappedAction {

    @Override
    protected void apply(@NonNull StringBuilder builder, @NonNull ErrorContext context) {
        builder.insert(context.getIndex() - 3, "void *");
    }
}
