package me.rochblondiaux.normus.utils;

import lombok.NonNull;
import me.rochblondiaux.normus.model.error.ErrorType;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class ParsingUtils {

    public static Optional<ErrorType> getErrorType(@NonNull String name) {
        return Arrays.stream(ErrorType.values())
                .filter(errorType -> errorType.name().equalsIgnoreCase(name))
                .findFirst();
    }

    public static String[] cleanArray(String[] array) {
        return Arrays.stream(array)
                .filter(x -> !x.isEmpty() && !x.isBlank())
                .toArray(String[]::new);
    }

    public static boolean isNumeric(@NonNull String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static Optional<Integer> getIntFromString(@NonNull String a) {
        a = a.replaceAll("[^0-9]", "");
        if (a.length() <= 0)
            return Optional.empty();
        return Optional.of(Integer.valueOf(a));
    }
}
