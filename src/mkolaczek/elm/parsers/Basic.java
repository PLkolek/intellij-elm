package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public class Basic {
    public static boolean sequence(PsiBuilder builder, Function<PsiBuilder, Boolean>... parsers) {
        for (Function<PsiBuilder, Boolean> parser : parsers) {
            if (!parser.apply(builder)) {
                return false;
            }
        }
        return true;
    }

    public static Function<PsiBuilder, Boolean> many(IElementType... tokens) {
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

    public static Function<PsiBuilder, Boolean> expect(IElementType... tokens) {
        return (PsiBuilder builder) -> simpleExpect(builder, tokens);
    }

    @NotNull
    private static Boolean simpleExpect(PsiBuilder builder, IElementType... tokens) {
        for (IElementType token : tokens) {
            if (builder.getTokenType() != token) {
                builder.error(token.toString() + " expected");
                return false;
            }
            builder.advanceLexer();
        }
        return true;
    }
}
