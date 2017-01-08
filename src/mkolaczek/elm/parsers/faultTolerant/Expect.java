package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.ImmutableSet;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class Expect extends FTParserAbstr {

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
        super(expectedToken.getName(), ImmutableSet.of(expectedToken), false, as);
    }


    @Override
    protected void parse2(PsiBuilder builder) {
        builder.advanceLexer();
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
    }
}
