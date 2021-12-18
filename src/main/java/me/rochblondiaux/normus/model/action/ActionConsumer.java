package me.rochblondiaux.normus.model.action;

import lombok.NonNull;
import me.rochblondiaux.normus.model.context.ErrorContext;

import java.util.List;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public interface ActionConsumer {

    List<String> patch(@NonNull ErrorContext context);

}
