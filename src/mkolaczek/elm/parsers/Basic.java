package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.Preconditions.checkArgument;

public class Basic {
    public static boolean sequence(PsiBuilder builder, Parser... parsers) {
        for (Parser parser : parsers) {
            if (!parser.apply(builder)) {
                return false;
            }
        }
        return true;
    }

    public static Parser many(IElementType... tokens) {
        checkArgument(tokens != null && tokens.length >= 1);
        return (PsiBuilder builder) -> {
            while (builder.getTokenType() == tokens[0]) {
                if (!simpleExpect(builder, tokens)) {
                    return false;
                }
            }
            return true;
        };
    }

    public static Parser expect(IElementType... tokens) {
        return (PsiBuilder builder) -> simpleExpect(builder, tokens);
    }

    @NotNull
    public static Boolean simpleExpect(PsiBuilder builder, IElementType... tokens) {
        for (IElementType token : tokens) {
            if (builder.getTokenType() != token) {
                builder.error(token.toString() + " expected");
                return false;
            }
            builder.advanceLexer();
        }
        return true;
    }

    public static Boolean or(PsiBuilder builder, Parser... parsers) {
        Marker start = builder.mark();
        for (Parser parser : parsers) {
            int offsetBefore = builder.getCurrentOffset();
            if (parser.apply(builder)) {
                start.drop();
                return true;
            }
            if (offsetBefore != builder.getCurrentOffset()) {
                start.drop();
            } else {
                start.rollbackTo();
            }
            start = builder.mark();
        }
        start.drop();
        builder.error("Someting expected in OR");
        return false;
    }

    public static boolean simpleOr(PsiBuilder builder, IElementType... tokens) {
        for (IElementType token : tokens) {
            if (builder.getTokenType() == token) {
                builder.advanceLexer();
                return true;
            }
        }
        builder.error("Someting expected in simpleOR");
        return false;
    }
}
