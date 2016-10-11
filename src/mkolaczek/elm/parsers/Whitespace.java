package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import org.jetbrains.annotations.NotNull;

public class Whitespace {

    public static boolean forcedWS(@NotNull PsiBuilder builder) {
        char lastChar = builder.getOriginalText().charAt(builder.getCurrentOffset());
        boolean ok = lastChar == ' ' || lastChar == '\t';
        if (!ok) {
            builder.error("Forced WS expected");
        }
        return ok;
    }

    public static boolean freshLine(@NotNull PsiBuilder builder) {
        boolean ok = builder.getOriginalText().charAt(builder.getCurrentOffset() - 1) == '\n';
        if (!ok) {
            builder.error("Fresh line expected");
        }
        return ok;
    }

    public static boolean maybeWhitespace(PsiBuilder builder) {
        boolean ok = builder.getOriginalText().charAt(builder.getCurrentOffset() - 1) != '\n';
        if (!ok) {
            builder.error("Whitespace expected");
        }
        return ok;
        /*Optional<IElementType> lastToken = whitespace(builder);
        boolean endsWithNewLine = lastToken.isPresent() && lastToken.get() == ElmTokenTypes.NEW_LINE;
        if (endsWithNewLine) {
            builder.error("Whitespace expected");
        }
        return !endsWithNewLine;*/
    }
}
