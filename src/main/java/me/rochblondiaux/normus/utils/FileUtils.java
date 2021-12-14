package me.rochblondiaux.normus.utils;

import lombok.NonNull;

import java.io.File;
import java.io.FileWriter;
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
        FileWriter writer = new FileWriter(file);
        writer.append(header);
        for (int i = 0; i < lines.size(); i++) {
            writer.append(System.getProperty("line.separator"));
            writer.append(lines.get(i));
        }
        writer.flush();
        writer.close();
    }

    public static void replace(@NonNull File file, @NonNull String key, @NonNull String value) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        FileWriter writer = new FileWriter(file);
        lines = lines.stream()
                .map(s -> s.replace(key, value))
                .collect(Collectors.toList());
        for (String line : lines) {
            writer.append(System.getProperty("line.separator"));
            writer.append(line);
        }
        writer.flush();
        writer.close();
    }
}
