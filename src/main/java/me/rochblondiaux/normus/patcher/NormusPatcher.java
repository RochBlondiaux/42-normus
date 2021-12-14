package me.rochblondiaux.normus.patcher;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;
import me.rochblondiaux.normus.model.error.NormeError;
import me.rochblondiaux.normus.model.file.FileState;
import me.rochblondiaux.normus.model.file.NormeFile;
import me.rochblondiaux.normus.model.regex.NormusRegexes;
import me.rochblondiaux.normus.parsing.NormusParser;
import me.rochblondiaux.normus.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
@Log(topic = "Patcher")
public class NormusPatcher {

    private final NormusParser parser;

    public int patch() throws IOException, InterruptedException {
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
        patchFile(files.get(0));
        AtomicInteger errors = new AtomicInteger();
        this.parser.parse()
                .forEach(file -> errors.addAndGet(file.getErrors().size()));
        return errors.get();
    }

    public void patchFile(@NonNull NormeFile file) {
        try {
            patchError(file, file.getErrors().get(0));
        } catch (IOException e) {
            log.severe(String.format("Cannot patch error at line %d in %s: %s", file.getErrors().get(0).getLine(), file.getFile().getPath(), e.getMessage()));
        }
    }

    public void patchError(@NonNull NormeFile normeFile, @NonNull NormeError error) throws IOException {
        File file = normeFile.getFile();
        List<String> lines = normeFile.getLines();
        System.out.println("Fixing " + error.getType().name() + "...");
        switch (error.getType()) {
            case CONSECUTIVE_SPC:
                break;
            case BRACE_NEWLINE:
                for (String line : normeFile.getLines()) {
                    Matcher matcher = NormusRegexes.BRACE.getMatcher(line);
                    if (matcher.find())
                        FileUtils.replace(file, matcher.group(), "\n{");
                }
                break;
            case INVALID_HEADER:
                FileUtils.insertHeader(file, HeaderGenerator.generateHeader(file));
                break;
            case BRACE_SHOULD_EOL:
                lines.add("\n");
                Files.write(file.toPath(), lines);
                break;
            case SPACE_BEFORE_FUNC:
            case TOO_MANY_TABS_FUNC:
                for (String line : normeFile.getLines()) {
                    Matcher matcher = NormusRegexes.FUNCTION.getMatcher(line);
                    if (matcher.find())
                        FileUtils.replace(file, matcher.group(), matcher.group().replaceAll("(\\s+)", "\t"));
                }
                break;
            case EMPTY_LINE_FILE_START:
                lines.remove(0);
                Files.write(file.toPath(), lines);
                break;
            case EMPTY_LINE_EOF:
                lines.remove(lines.size() - 1);
                Files.write(file.toPath(), lines);
            case EMPTY_LINE_FUNCTION:
            case CONSECUTIVE_NEWLINES:
                if (error.getLine() >= lines.size())
                    return;
                lines.remove(error.getLine() - 1);
                Files.write(file.toPath(), lines);
                break;
            case MISALIGNED_VAR_DECL:
                break;
        }
    }
}
