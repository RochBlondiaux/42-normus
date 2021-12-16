package me.rochblondiaux.normus.utils;

import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class FileUtils {

    public static void insertHeader(@NonNull File file, @NonNull String header) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        lines.add(0, header);
        Files.write(file.toPath(), lines);
    }

    public static void replace(@NonNull File file, @NonNull String key, @NonNull String value) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        lines = lines.stream()
                .map(s -> s.replace(key, value))
                .collect(Collectors.toList());
        Files.write(file.toPath(), lines);
    }

    public static void updateLine(@NonNull File file, int index, String line) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        lines.set(index, line);
        Files.write(file.toPath(), lines);
    }

    public static void insertLine(@NonNull File file, int index, String line) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        lines.add(index, line);
        Files.write(file.toPath(), lines);
    }
}
