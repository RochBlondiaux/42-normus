package me.rochblondiaux.normus.service;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;
import me.rochblondiaux.normus.model.action.Action;
import me.rochblondiaux.normus.model.context.ErrorContext;
import me.rochblondiaux.normus.model.error.ErrorType;
import me.rochblondiaux.normus.model.error.NormeError;
import me.rochblondiaux.normus.model.file.NormeFile;
import me.rochblondiaux.normus.model.patch.misc.*;
import me.rochblondiaux.normus.model.patch.regex.*;
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
        this.repository.add(ErrorType.NO_ARGS_VOID, new AddVoidArgAction());
        this.repository.add(ErrorType.RETURN_PARENTHESIS, new AddParenthesisAction());
        this.repository.add(ErrorType.GLOBAL_VAR_NAMING, new GlobalRenameAction());
        this.repository.add(new AddSpaceParenthesisAction(), ErrorType.SPACE_AFTER_KW, ErrorType.SPC_BFR_PAR);
        this.repository.add(ErrorType.USER_DEFINED_TYPEDEF, new RenameTypeDefAction());

        /* Misc */
        this.repository.add(ErrorType.BRACE_SHOULD_EOL, new AddNewLineAction());
        this.repository.add(ErrorType.INVALID_HEADER, new InsertHeaderAction());
        this.repository.add(new RemoveLineAction(), ErrorType.EMPTY_LINE_FILE_START,
                ErrorType.EMPTY_LINE_EOF,
                ErrorType.EMPTY_LINE_FUNCTION,
                ErrorType.CONSECUTIVE_NEWLINES,
                ErrorType.WRONG_SCOPE_COMMENT,
                ErrorType.INCLUDE_HEADER_ONLY);
        this.repository.add(ErrorType.SPACE_EMPTY_LINE, new DeleteCharacterAction());
        this.repository.add(ErrorType.MISSING_IDENTIFIER, new AddIdentifierAction());
        this.repository.add(ErrorType.FORBIDDEN_CHAR_NAME, new LowercaseAction());
        this.repository.add(ErrorType.SPC_BEFORE_NL, new RemoveSpaceAction());
        this.repository.add(ErrorType.NL_AFTER_VAR_DECL, new InsertEmptyLineAction());

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
