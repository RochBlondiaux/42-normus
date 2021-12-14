package me.rochblondiaux.normus.parsing;

import lombok.Data;
import lombok.NonNull;
import me.rochblondiaux.normus.model.error.NormeError;
import me.rochblondiaux.normus.model.executor.NormusExecutor;
import me.rochblondiaux.normus.model.file.FileState;
import me.rochblondiaux.normus.model.file.NormeFile;
import me.rochblondiaux.normus.model.regex.NormusRegexes;
import me.rochblondiaux.normus.utils.ParsingUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
public class NormusParser {

    private final NormusExecutor executor;

    public List<NormeFile> parse() throws IOException, InterruptedException {
        List<NormeFile> files = new ArrayList<>();
        List<String> lines = executor.call();

        NormeFile normeFile = null;
        for (String line : lines) {
            Matcher matcher = NormusRegexes.FILE_HEADER.getMatcher(line);
            if (matcher.find() && isFileHeader(line)) {
                File file = new File(matcher.group());
                if (!file.exists())
                    continue;
                normeFile = new NormeFile(file);
                if (NormusRegexes.FILE_OK_HEADER.getMatcher(line).find())
                    normeFile.setState(FileState.CLEAN);
                normeFile.getLines().addAll(Files.readAllLines(file.toPath()));
                files.add(normeFile);
                continue;
            }
            if (Objects.isNull(normeFile))
                continue;
            NormeFile finalNormeFile = normeFile;
            parseError(line).ifPresent(n -> finalNormeFile.getErrors().add(n));
        }
        return files;
    }

    private Optional<NormeError> parseError(@NonNull String line) {
        String[] words = ParsingUtils.cleanArray(line.split(" "));
        return ParsingUtils.getErrorType(words[1])
                .map(errorType -> new NormeError(errorType,
                        ParsingUtils.getIntFromString(words[3]).orElse(-1),
                        ParsingUtils.getIntFromString(words[5]).orElse(-1)));
    }

    private boolean isFileHeader(@NonNull String line) {
        return NormusRegexes.FILE_OK_HEADER.getMatcher(line).find()
                || NormusRegexes.FILE_ERROR_HEADER.getMatcher(line).find();
    }
}
