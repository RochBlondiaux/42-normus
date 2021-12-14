package me.rochblondiaux.normus.model.executor;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
@Log(topic = "Norminette Executor")
public class NormusExecutor {

    private final String command;
    private final Runtime runtime;

    public NormusExecutor() {
        this.command = "norminette";
        this.runtime = Runtime.getRuntime();
    }

    public List<String> call(@NonNull File file) throws IOException, InterruptedException {
        File tmp = Files.createTempFile("norminette " + file.getPath(), null).toFile();
        return executeAndReadOutputs(tmp);
    }

    public List<String> call() throws IOException, InterruptedException {
        File tmp = Files.createTempFile("norminette", null).toFile();
        return executeAndReadOutputs(tmp);
    }

    private List<String> executeAndReadOutputs(File tmp) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectOutput(tmp);
        pb.directory(Paths.get("").toAbsolutePath().toFile());
        Process process = pb.start();
        process.waitFor();
        tmp.deleteOnExit();
        return Files.readAllLines(tmp.toPath());
    }

}
