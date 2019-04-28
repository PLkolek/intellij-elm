package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;
import mkolaczek.elm.psi.Element;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static java.util.Arrays.stream;

@SuppressWarnings("SameParameterValue")
public interface Parser {

    enum Result {
        OK, ERROR, SKIPPED
    }

    Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context);

    @NotNull
    WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead);

    boolean isRequired();

    String name();

    default boolean willParse(PsiBuilder psiBuilder, Indentation indentation) {
        PsiBuilder.Marker marker = psiBuilder.mark();
        boolean result = willParse(psiBuilder, indentation, 1).isSuccess();
        marker.rollbackTo();
        return result;
    }

    default Parser as(Element as) {
        return new As(this, as, As.Mode.SKIP_EMPTY);
    }

    default Parser as(Element as, As.Mode mode) {
        return new As(this, as, mode);
    }

    default Parser swapAs(Element as) {
        return new SwapAs(this, as);
    }


    default Parser llk(int lookahead) {
        return new LLK(this, lookahead);
    }

    static boolean anyRequired(Parser... parsers) {
        return stream(parsers).anyMatch(Parser::isRequired);
    }

    static boolean allRequired(Parser... parsers) {
        return stream(parsers).allMatch(Parser::isRequired);
    }
}
