package me.rochblondiaux.normus.model.executor;

import lombok.Data;
import lombok.extern.java.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
@Log(topic = "Norminette Executor")
public class NorminetteExecutor {

    private final String command;
    private final Runtime runtime;

    public NorminetteExecutor() {
        this.command = "norminette";
        this.runtime = Runtime.getRuntime();
    }

    public List<String> call() throws IOException, InterruptedException {
        File tmp = Files.createTempFile("norminette", null).toFile();
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectOutput(tmp);
        pb.directory(Paths.get("").toAbsolutePath().toFile());
        Process process = pb.start();
        process.waitFor();
        tmp.deleteOnExit();
        return Files.readAllLines(tmp.toPath());
    }
}
