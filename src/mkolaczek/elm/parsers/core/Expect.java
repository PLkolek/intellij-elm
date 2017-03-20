package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
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
    public boolean parse(PsiBuilder psiBuilder, Collection<Parser> nextParsers) {
        if (psiBuilder.eof() || expectedToken != psiBuilder.getTokenType()) {
            return false;
        }
        psiBuilder.advanceLexer();
        return true;
    }

    @Override
    public boolean willParse(PsiBuilder psiBuilder) {
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
