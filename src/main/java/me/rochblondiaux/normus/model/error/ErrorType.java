package me.rochblondiaux.normus.model.error;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 *
 * Get from https://github.com/42School/norminette/blob/master/norminette/norm_error.py
 */
public enum ErrorType {
    INVALID_HEADER,
    SPACE_BEFORE_FUNC,
    BRACE_NEWLINE,
    BRACE_SHOULD_EOL,
    EMPTY_LINE_FILE_START,
    TOO_MANY_TABS_FUNC,
    EMPTY_LINE_FUNCTION,
    EMPTY_LINE_EOF,
    CONSECUTIVE_NEWLINES,
    SPACE_REPLACE_TAB,
    TAB_INSTEAD_SPC,
    SPACE_EMPTY_LINE,
    WRONG_SCOPE_COMMENT,
    INCLUDE_HEADER_ONLY,
    CONSECUTIVE_SPC,
    RETURN_PARENTHESIS,
    FORBIDDEN_CHAR_NAME,
    MISSING_IDENTIFIER,
    SPC_BEFORE_NL,
    USER_DEFINED_TYPEDEF,
    GLOBAL_VAR_NAMING,

    // NOT IMPLEMENTED YET
    LINE_TOO_LONG,
    MISALIGNED_VAR_DECL,
    WRONG_SCOPE,
    FORBIDDEN_CS,
}
