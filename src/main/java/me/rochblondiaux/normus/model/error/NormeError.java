package me.rochblondiaux.normus.model.error;

import lombok.Data;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
public class NormeError {

    private final ErrorType type;
    private final int line;
    private final int index;

}
