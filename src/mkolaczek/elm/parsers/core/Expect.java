package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.psi.Token;

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
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Indentation indentation) {
        if (builder.eof() || expectedToken != builder.getTokenType()) {
            return Result.TOKEN_ERROR;
        }
        builder.advanceLexer();
        return Result.OK;
    }

    @Override
    public boolean willParse(PsiBuilder psiBuilder, Indentation indentation) {
        return psiBuilder.getTokenType() == expectedToken;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public String name() {
        return expectedToken.getName();
    }
}
