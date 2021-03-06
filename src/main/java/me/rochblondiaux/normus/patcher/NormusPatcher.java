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
import me.tongfei.progressbar.ProgressBar;

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

    public int patch(@NonNull ProgressBar pb) throws IOException, InterruptedException {
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
        patchFile(pb, files.get(0));
        AtomicInteger errors = new AtomicInteger();
        this.parser.parse()
                .forEach(file -> errors.addAndGet(file.getErrors().size()));
        return errors.get();
    }

    public void patchFile(@NonNull ProgressBar pb, @NonNull NormeFile file) {
        try {
            pb.setExtraMessage("Patching " + file.getErrors().get(0).getType().name());
            patchError(file, file.getErrors().get(0));
        } catch (IOException e) {
            log.severe(String.format("Cannot patch error at line %d in %s: %s", file.getErrors().get(0).getLine(), file.getFile().getPath(), e.getMessage()));
        }
    }

    public void patchError(@NonNull NormeFile normeFile, @NonNull NormeError error) throws IOException {
        File file = normeFile.getFile();
        List<String> lines = normeFile.getLines();
        Matcher matcher;
        int index = error.getLine() - 1;
        String line = normeFile.getLines().get(index);
        StringBuilder builder = new StringBuilder(line);
        if (error.getLine() >= lines.size())
            return;

        switch (error.getType()) {
            case CONSECUTIVE_SPC:
                FileUtils.updateLine(file, index, HeaderGenerator.remove_spaces(line, "", 1));
                break;
            case TAB_INSTEAD_SPC:
                builder.setCharAt(error.getIndex(), ' ');
                FileUtils.updateLine(file, index, builder.toString());
                break;
            case SPACE_REPLACE_TAB:
                matcher = NormusRegexes.WHITE_SPACES.getMatcher(line);
                if (!matcher.find())
                    return;
                FileUtils.updateLine(file, index, matcher.replaceAll(matchResult -> {
                    double tabs = matchResult.group().length() / 4d;
                    if (tabs == 0)
                        tabs += 1;
                    return "\t".repeat((int) Math.ceil(tabs));
                }));
                break;
            case USER_DEFINED_TYPEDEF:
                matcher = NormusRegexes.TYPE_DEF.getMatcher(line);
                if (matcher.find() && matcher.groupCount() >= 3)
                    FileUtils.updateLine(file, index, line.replace(matcher.group(3), "t_" + matcher.group(3)));
                break;

            case LINE_TOO_LONG:
                // I don't know wtd w this one too
                // I may implement it later, but I'm not sure at all
                break;

            case MISALIGNED_VAR_DECL:
            case WRONG_SCOPE:
            case FORBIDDEN_CS:
                // TODO: Code those issue patchers
                break;

            // TODO: Manage to change structures, union, enum, globals, ... name according to this pdf: https://github.com/42School/norminette/blob/master/pdf/fr.norme.pdf
            // TODO: Change variable name too
            // TODO: & filename ^^
            // TODO: Remove non ascii characters from code & filename, variable name, ...
            // TODO: Check for forbidden strctures
            // TODO: Fix comments
        }
    }
}
