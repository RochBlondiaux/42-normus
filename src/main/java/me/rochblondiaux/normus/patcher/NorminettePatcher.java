package me.rochblondiaux.normus.patcher;

import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.rochblondiaux.normus.model.error.NormeError;
import me.rochblondiaux.normus.model.file.FileState;
import me.rochblondiaux.normus.model.file.NormeFile;
import me.rochblondiaux.normus.utils.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
public class NorminettePatcher {

    private final List<NormeFile> files;
    private static final Pattern bracePattern = Pattern.compile("[\\s+]*[{]");
    private static final Pattern functionPattern = Pattern.compile("^(\\w+(\\s+)?){2,}\\([^!@#$+%^]+?\\)");

    public void patch() {
        files.stream()
                .filter(file -> file.getState().equals(FileState.DIRTY))
                .forEach(this::patchFile);
    }

    private void patchFile(@NonNull NormeFile file) {
        file.getErrors()
                .forEach(error -> patchError(file, error));
    }

    @SneakyThrows
    private void patchError(@NonNull NormeFile normeFile, @NonNull NormeError error) {
        File file = normeFile.getFile();
        List<String> lines = normeFile.getLines();
        switch (error.getType()) {
            case CONSECUTIVE_SPC:
                break;
            case BRACE_NEWLINE:
                for (String line : normeFile.getLines()) {
                    Matcher matcher = bracePattern.matcher(line);
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
                    Matcher matcher = functionPattern.matcher(line);
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
