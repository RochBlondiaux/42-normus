package me.rochblondiaux.normus;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import me.rochblondiaux.normus.model.executor.NormusExecutor;
import me.rochblondiaux.normus.parsing.NormusParser;
import me.rochblondiaux.normus.patcher.NormusPatcher;
import me.tongfei.progressbar.ProgressBar;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Log(topic = "Normus")
@Getter
public class Normus {

    private final NormusExecutor executor;
    private final NormusParser parser;
    private final NormusPatcher patcher;

    @SneakyThrows
    public Normus() {
        int remainingErrors = 100;

        this.executor = new NormusExecutor();
        this.parser = new NormusParser(executor);
        this.patcher = new NormusPatcher(parser);
        try (ProgressBar pb = new ProgressBar("Normus", remainingErrors)) {
            pb.setExtraMessage("Patching...");
            pb.maxHint((remainingErrors = patcher.patch()) + 1);
            while (remainingErrors > 0)
            {
                remainingErrors = patcher.patch();
                pb.stepTo(pb.getMax() - remainingErrors);
            }
        }
    }

    public static void main(String[] args) {
        new Normus();
    }
}
