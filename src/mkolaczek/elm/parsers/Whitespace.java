package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * When parsing whitespace, errors are inserted into AST, but ignored in whole parsing.
 * I don't want to screw everything just because somebody forgot to indent a line.
 */
public class Whitespace {

    public static boolean forcedWS(@NotNull PsiBuilder builder) {

        char lastChar = builder.getOriginalText().charAt(builder.getCurrentOffset() - 1);
        if (lastChar != ' ' && lastChar != '\t') {
            builder.error("Forced WS expected");
        }
        return true;
    }

    public static boolean freshLine(@NotNull PsiBuilder builder) {
        if (!isFreshLine(builder)) {
            builder.error("Fresh line expected");
        }
        return true;
    }

    public static boolean isFreshLine(@NotNull PsiBuilder builder) {
        return builder.getCurrentOffset() <= 0 || builder.getOriginalText()
                                                         .charAt(builder.getCurrentOffset() - 1) == '\n';
    }

    public static boolean maybeWhitespace(PsiBuilder builder) {
        if (builder.getOriginalText().charAt(builder.getCurrentOffset() - 1) == '\n') {
            builder.error("Whitespace expected");
        }
        return true;
    }

    public static boolean noWhitespace(PsiBuilder builder) {
        if (Character.isWhitespace(builder.getOriginalText().charAt(builder.getCurrentOffset() - 1))) {
            builder.error("Expected no whitespace");
        }
        return true;
    }
}
