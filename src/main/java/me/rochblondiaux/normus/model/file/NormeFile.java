package me.rochblondiaux.normus.model.file;

import lombok.Data;
import lombok.Setter;
import me.rochblondiaux.normus.model.error.NormeError;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
@Setter
public class NormeFile {

    private final File file;
    private FileState state = FileState.DIRTY;
    private final List<String> lines = new LinkedList<>();
    private final List<NormeError> errors = new LinkedList<>();

}
