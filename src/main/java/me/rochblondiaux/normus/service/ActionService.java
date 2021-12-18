package me.rochblondiaux.normus.service;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;
import me.rochblondiaux.normus.model.action.Action;
import me.rochblondiaux.normus.model.context.ErrorContext;
import me.rochblondiaux.normus.model.error.ErrorType;
import me.rochblondiaux.normus.model.error.NormeError;
import me.rochblondiaux.normus.model.file.NormeFile;
import me.rochblondiaux.normus.model.patch.misc.EmptyLineAction;
import me.rochblondiaux.normus.model.patch.misc.InvalidHeaderAction;
import me.rochblondiaux.normus.model.patch.regex.BraceNewLineAction;
import me.rochblondiaux.normus.model.patch.misc.BraceShouldEolAction;
import me.rochblondiaux.normus.model.patch.regex.SpaceBeforeFunctionAction;
import me.rochblondiaux.normus.repository.ActionRepository;

import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
@Log(topic = "Action Service")
public class ActionService {

    private final ActionRepository repository;

    public ActionService() {
        this.repository = new ActionRepository();

        /* Regexes */
        this.repository.add(ErrorType.BRACE_NEWLINE, new BraceNewLineAction());
        this.repository.add(new SpaceBeforeFunctionAction(), ErrorType.SPACE_BEFORE_FUNC, ErrorType.TOO_MANY_TABS_FUNC);

        /* Misc */
        this.repository.add(ErrorType.BRACE_SHOULD_EOL, new BraceShouldEolAction());
        this.repository.add(ErrorType.INVALID_HEADER, new InvalidHeaderAction());
        this.repository.add(new EmptyLineAction(), ErrorType.EMPTY_LINE_FILE_START, ErrorType.EMPTY_LINE_EOF);
    }

    public void apply(@NonNull NormeFile file, @NonNull NormeError error) {
        ErrorContext context = new ErrorContext(file.getFile().getName(),
                file.getLines().get(error.getLine()),
                error.getLine(),
                error.getIndex(),
                file.getLines());
        repository.getByErrorType(error.getType())
                .map(Action::getConsumer)
                .ifPresent(consumer -> {
                    consumer.patch(context);
                    try {
                        Files.write(file.getFile().toPath(), context.getLines());
                    } catch (IOException e) {
                        log.severe(String.format("Cannot update file: %s", e.getMessage()));
                    }
                });
    }
}
