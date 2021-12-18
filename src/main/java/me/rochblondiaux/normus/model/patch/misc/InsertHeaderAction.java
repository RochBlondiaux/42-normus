package me.rochblondiaux.normus.model.patch.misc;

import lombok.NonNull;
import me.rochblondiaux.normus.model.action.ActionConsumer;
import me.rochblondiaux.normus.model.context.ErrorContext;
import me.rochblondiaux.normus.patcher.HeaderGenerator;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class InsertHeaderAction implements ActionConsumer {

    @Override
    public void patch(@NonNull ErrorContext context) {
        context.getLines().add(0, HeaderGenerator.generateHeader(context.getFilename()));
    }

}
