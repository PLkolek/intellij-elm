package mkolaczek.elm.parsers.core;

import com.google.common.collect.ImmutableSet;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class Expect extends ParserAbstr {

    private final Token expectedToken;

    public static Expect expect(Token expectedToken) {
        return new Expect(expectedToken);
    }

    public static Expect expectAs(Token expectedToken, Element as) {
        return new Expect(expectedToken, as);
    }

    private Expect(Token expectedToken) {
        this(expectedToken, null);
    }

    private Expect(Token expectedToken, Element as) {
        super(expectedToken.getName(), false, as);
        this.expectedToken = expectedToken;
    }


    @Override
    protected void parse2(PsiBuilder builder) {
        builder.advanceLexer();
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
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
