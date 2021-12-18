package me.rochblondiaux.normus;

import lombok.NonNull;
import lombok.extern.java.Log;
import me.rochblondiaux.normus.model.executor.NormusExecutor;
import me.rochblondiaux.normus.model.file.FileState;
import me.rochblondiaux.normus.model.file.NormeFile;
import me.rochblondiaux.normus.parsing.NormusParser;
import me.rochblondiaux.normus.service.ActionService;
import me.tongfei.progressbar.ProgressBar;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Log(topic = "Normus")
public class Normus {

    private static Normus instance;

    private final NormusExecutor executor;
    private final NormusParser parser;
    private final ActionService actionService;

    public Normus() {
        instance = this;
        this.executor = new NormusExecutor();
        this.parser = new NormusParser(executor);
        this.actionService = new ActionService();

        start();
    }

    public void start() {
        int remainingErrors = 100;

        try (ProgressBar pb = new ProgressBar("Normus", remainingErrors)) {
            pb.setExtraMessage("Patching...");
            pb.maxHint((remainingErrors = patch(pb)) + 1);
            while (remainingErrors > 0) {
                remainingErrors = patch(pb);
                pb.stepTo(pb.getMax() - remainingErrors);
            }
        }
    }

    public int patch(@NonNull ProgressBar progressBar) {
        List<NormeFile> files;
        try {
            files = parser.parse()
                    .stream()
                    .filter(file -> file.getState().equals(FileState.DIRTY))
                    .collect(Collectors.toList());
        } catch (IOException | InterruptedException e) {
            log.severe(String.format("Cannot parse norminette outputs: %s", e.getMessage()));
            return 0;
        }
        if (files.size() == 0) {
            log.info("All files were patched successfully!");
            return 0;
        }
        patchFile(progressBar, files.get(0));
        AtomicInteger errors = new AtomicInteger();
        try {
            this.parser.parse()
                    .forEach(file -> errors.addAndGet(file.getErrors().size()));
        } catch (IOException | InterruptedException e) {
            log.severe(String.format("Cannot parse norminette outputs: %s", e.getMessage()));
        }
        return errors.get();
    }

    public void patchFile(@NonNull ProgressBar pb, @NonNull NormeFile file) {
        pb.setExtraMessage("Patching " + file.getErrors().get(0).getType().name());
        actionService.apply(file, file.getErrors().get(0));
    }

    public static Normus get() {
        return instance;
    }
}
