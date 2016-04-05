package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Whitespace {

    private static Optional<IElementType> whitespace(@NotNull PsiBuilder builder) {
        IElementType lastToken = null;
        while (builder.getTokenType() == ElmTypes.WHITE_SPACE || builder.getTokenType() == ElmTypes.NEW_LINE) {
            lastToken = builder.getTokenType();
            builder.advanceLexer();
        }
        return Optional.ofNullable(lastToken);
    }

    public static boolean forcedWS(@NotNull PsiBuilder builder) {
        Optional<IElementType> lastToken = whitespace(builder);
        boolean endsWithWS = lastToken.isPresent() && lastToken.get() == ElmTypes.WHITE_SPACE;
        if (!endsWithWS) {
            builder.error("Whitespace expected");
        }
        return endsWithWS;
    }

    public static boolean freshLine(@NotNull PsiBuilder builder) {
        Optional<IElementType> lastToken = whitespace(builder);
        boolean endsWithNewline = lastToken.isPresent() && lastToken.get() == ElmTypes.NEW_LINE;
        if (!endsWithNewline) {
            builder.error("New line expected");
        }
        return endsWithNewline;
    }

    public static boolean maybeWhitespace(PsiBuilder builder) {
        Optional<IElementType> lastToken = whitespace(builder);
        boolean endsWithNewLine = lastToken.isPresent() && lastToken.get() == ElmTypes.NEW_LINE;
        if (endsWithNewLine) {
            builder.error("Whitespace expected");
        }
        return !endsWithNewLine;
    }
}
