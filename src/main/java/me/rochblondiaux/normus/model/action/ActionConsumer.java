package me.rochblondiaux.normus.model.action;

import lombok.NonNull;
import me.rochblondiaux.normus.model.context.ErrorContext;

import java.util.List;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public interface ActionConsumer {

   void patch(@NonNull ErrorContext context);

}
