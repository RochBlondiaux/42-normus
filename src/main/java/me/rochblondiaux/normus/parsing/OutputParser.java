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
import java.util.ArrayList;
import java.util.List;
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
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            Matcher matcher = pattern.matcher(line);
            if (!matcher.find() || !isFileHeader(line))
                continue;
            File file = new File(matcher.group());
            if (!file.exists())
                continue;
            NormeFile normeFile = new NormeFile(file);
            files.add(normeFile);
            if (okPattern.matcher(line).find()) {
                normeFile.setState(FileState.CLEAN);
                continue;
            }
            while ((line = lines.get(i++)) != null) {
                parseLine(line).ifPresent(normeError -> normeFile.getErrors().add(normeError));
                if (lines.size() <= i || (lines.size() > i + 1 && isFileHeader(lines.get(i + 1))))
                    break;
            }
        }
    }

    private Optional<NormeError> parseLine(@NonNull String line) {
        line = line.replace("Error: ", "");
        String[] words = ParsingUtils.cleanArray(line.split(" "));
        return ParsingUtils.getErrorType(words[0])
                .map(errorType -> new NormeError(errorType,
                        ParsingUtils.getIntFromString(words[2]).orElse(-1),
                        ParsingUtils.getIntFromString(words[4]).orElse(-1)));
    }

    private boolean isFileHeader(@NonNull String line) {
        return errorPattern.matcher(line).find()
                || okPattern.matcher(line).find();
    }
}
