package me.rochblondiaux.normus.repository;

import lombok.Data;
import lombok.NonNull;
import me.rochblondiaux.normus.model.action.Action;
import me.rochblondiaux.normus.model.error.ErrorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
@Data
public class ActionRepository {

    private final List<Action> actions = new ArrayList<>();

    public void add(@NonNull Action action) {
        if (isRegistered(action.getErrorType()))
            throw new IllegalStateException(String.format("An action is already registered for %s error!", action.getErrorType().name()));
        this.actions.add(action);
    }

    public void remove(@NonNull Action action) {
        this.actions.remove(action);
    }

    public Optional<Action> getByErrorType(@NonNull ErrorType type) {
        return actions.stream()
                .filter(action -> action.getErrorType().equals(type))
                .findFirst();
    }

    public boolean isRegistered(@NonNull ErrorType type) {
        return actions.stream()
                .anyMatch(action -> action.getErrorType().equals(type));
    }
}
