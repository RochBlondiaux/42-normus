package me.rochblondiaux.normus.model.patch.misc;

import lombok.NonNull;
import me.rochblondiaux.normus.model.action.ActionConsumer;
import me.rochblondiaux.normus.model.context.ErrorContext;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class RemoveLineAction implements ActionConsumer {

    @Override
    public void patch(@NonNull ErrorContext context) {
        context.getLines().remove(context.getLineIndex());
    }

}
