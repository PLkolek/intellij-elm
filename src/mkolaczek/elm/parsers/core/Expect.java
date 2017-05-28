package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;
import mkolaczek.elm.psi.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


public class Expect implements Parser {

    private final Token expectedToken;

    public static Expect expect(Token expectedToken) {
        return new Expect(expectedToken);
    }

    private Expect(Token expectedToken) {
        this.expectedToken = expectedToken;
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context) {
        if (builder.eof() || expectedToken != builder.getTokenType()) {
            return Result.TOKEN_ERROR;
        }
        builder.advanceLexer();
        return Result.OK;
    }

    @NotNull
    @Override
    public WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead) {
        if (psiBuilder.getTokenType() == expectedToken) {
            if (lookahead > 1) {
                psiBuilder.advanceLexer();
            }
            return WillParseResult.success(lookahead - 1);
        }
        return WillParseResult.failure();
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public String name() {
        return expectedToken.getName();
    }

    @Override
    public String toString() {
        return "expect(" + name() + ")";
    }
}
