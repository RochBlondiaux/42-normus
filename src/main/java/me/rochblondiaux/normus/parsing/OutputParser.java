package me.rochblondiaux.normus.parsing;

import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.rochblondiaux.normus.model.error.NormeError;
import me.rochblondiaux.normus.model.executor.NorminetteExecutor;
import me.rochblondiaux.normus.model.file.FileState;
import me.rochblondiaux.normus.model.file.NormeFile;
import me.rochblondiaux.normus.utils.ParsingUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
public class OutputParser {

    private final NorminetteExecutor executor;
    private final List<NormeFile> files = new ArrayList<>();

    private final Pattern pattern = Pattern.compile(".*[.][a-zA-Z0-9]*");
    private final Pattern errorPattern = Pattern.compile(".*[.][a-zA-Z0-9]*: Error!");
    private final Pattern okPattern = Pattern.compile(".*[.][a-zA-Z0-9]*: OK!");

    public OutputParser() {
        this.executor = new NorminetteExecutor();
    }

    @SneakyThrows
    public void parse() {
        List<String> lines = executor.call();
        NormeFile normeFile = null;
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isFileHeader(line)) {
                File file = new File(matcher.group());
                if (!file.exists())
                    continue;
                normeFile = new NormeFile(file);
                if (okPattern.matcher(line).find())
                    normeFile.setState(FileState.CLEAN);
                normeFile.getLines().addAll(Files.readAllLines(file.toPath()));
                files.add(normeFile);
                continue;
            }
            if (Objects.isNull(normeFile))
                continue;
            NormeFile finalNormeFile = normeFile;
            parseLine(line).ifPresent(normeError -> finalNormeFile.getErrors().add(normeError));
        }
    }

    private Optional<NormeError> parseLine(@NonNull String line) {
        String[] words = ParsingUtils.cleanArray(line.split(" "));
        System.out.println(" ");
        return ParsingUtils.getErrorType(words[1])
                .map(errorType -> new NormeError(errorType,
                        ParsingUtils.getIntFromString(words[3]).orElse(-1),
                        ParsingUtils.getIntFromString(words[5]).orElse(-1)));
    }

    private boolean isFileHeader(@NonNull String line) {
        return errorPattern.matcher(line).find()
                || okPattern.matcher(line).find();
    }
}
