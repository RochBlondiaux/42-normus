package me.rochblondiaux.normus.model.context;

import lombok.Data;

import java.util.List;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
public class ErrorContext {

    private final String filename;
    private final String line;
    private final int lineIndex;
    private final int index;
    private final List<String> lines;
}
