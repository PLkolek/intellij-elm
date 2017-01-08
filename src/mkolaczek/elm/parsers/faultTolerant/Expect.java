package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.ImmutableSet;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class Expect extends FTParserAbstr {

    protected Expect(String name, Token expectedToken) {
        this(name, expectedToken, null);
    }

    public Expect(String name, Token expectedToken, Element as) {
        super(name, ImmutableSet.of(expectedToken), false, as);
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        builder.advanceLexer();
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
    }
}
