package me.rochblondiaux.normus;

import lombok.extern.java.Log;
import me.rochblondiaux.normus.parsing.OutputParser;
import me.rochblondiaux.normus.patcher.NorminettePatcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Log(topic = "Normus")
public class Normus {


    public static void main(String[] args) {
        OutputParser parser = new OutputParser();
        parser.parse();
        NorminettePatcher patcher = new NorminettePatcher(parser.getFiles());
        patcher.patch();
    }
}
