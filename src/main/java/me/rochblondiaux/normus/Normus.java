package me.rochblondiaux.normus;

import lombok.extern.java.Log;
import me.rochblondiaux.normus.model.file.FileState;
import me.rochblondiaux.normus.parsing.OutputParser;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Log(topic = "Normus")
public class Normus {


    public static void main(String[] args) {
        OutputParser parser = new OutputParser();
        parser.parse();
        log.info(String.format("I found %d errors in %d files!", 0, parser.getFiles()
                .stream()
                .filter(normeFile -> normeFile.getState().equals(FileState.DIRTY))
                .count()));
    }
}
