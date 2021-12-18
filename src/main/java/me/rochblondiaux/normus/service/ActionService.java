package me.rochblondiaux.normus.service;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;
import me.rochblondiaux.normus.model.action.Action;
import me.rochblondiaux.normus.model.context.ErrorContext;
import me.rochblondiaux.normus.model.error.NormeError;
import me.rochblondiaux.normus.model.file.NormeFile;
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
    }

    public void apply(@NonNull NormeFile file, @NonNull NormeError error) {
        ErrorContext context = new ErrorContext(file.getFile().getName(),
                file.getLines().get(error.getLine()),
                error.getIndex(),
                file.getLines());
        repository.getByErrorType(error.getType())
                .map(Action::getConsumer)
                .map(actionConsumer -> actionConsumer.patch(context))
                .ifPresent(strings -> {
                    try {
                        Files.write(file.getFile().toPath(), strings);
                    } catch (IOException e) {
                        log.severe(String.format("Cannot update file: %s", e.getMessage()));
                    }
                });
    }
}
