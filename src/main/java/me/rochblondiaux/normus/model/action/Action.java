package me.rochblondiaux.normus.model.action;

import lombok.Data;
import me.rochblondiaux.normus.model.error.ErrorType;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
public class Action {

    private final ActionType type;
    private final ErrorType errorType;
    private final ActionConsumer consumer;
}
