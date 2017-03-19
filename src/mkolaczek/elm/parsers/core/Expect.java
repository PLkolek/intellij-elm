package mkolaczek.elm.parsers.core;

import com.google.common.collect.ImmutableSet;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class Expect implements Parser {

    private final Token expectedToken;

    public static Expect expect(Token expectedToken) {
        return new Expect(expectedToken);
    }

    private Expect(Token expectedToken) {
        this.expectedToken = expectedToken;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens) {
        if (psiBuilder.eof() || expectedToken != psiBuilder.getTokenType()) {
            return false;
        }
        psiBuilder.advanceLexer();
        return true;
    }

    @Override
    public Set<Token> startingTokens() {
        return ImmutableSet.of(expectedToken);
    }

    @Override
    public Set<Token> secondTokens() {
        throw new UnsupportedOperationException("Expect contains only one token");
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
