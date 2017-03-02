package mkolaczek.elm.parsers.core;

import com.google.common.collect.ImmutableSet;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class Expect extends ParserAbstr {

    private final Token expectedToken;

    public static Expect expect(Token expectedToken) {
        return new Expect(expectedToken);
    }

    private Expect(Token expectedToken) {
        super(expectedToken.getName(), false);
        this.expectedToken = expectedToken;
    }


    @Override
    protected void parse2(PsiBuilder builder, Set<Token> nextTokens) {
        builder.advanceLexer();
    }

    @Override
    public Set<Token> startingTokens() {
        return ImmutableSet.of(expectedToken);
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
