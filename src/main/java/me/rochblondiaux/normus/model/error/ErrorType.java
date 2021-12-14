package me.rochblondiaux.normus.model.error;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public enum ErrorType {
    INVALID_HEADER,
    CONSECUTIVE_SPC,
    SPACE_BEFORE_FUNC,
    BRACE_NEWLINE,
    BRACE_SHOULD_EOL,
    EMPTY_LINE_FILE_START,
    TOO_MANY_TABS_FUNC,
    EMPTY_LINE_FUNCTION,
    EMPTY_LINE_EOF,
    CONSECUTIVE_NEWLINES,
    MISALIGNED_VAR_DECL,
    SPACE_REPLACE_TAB
}
