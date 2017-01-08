package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;

public class PaddedComma extends FTParserAbstr {

    private final Padded padded;

    protected PaddedComma() {
        super(",", Sets.newHashSet(Tokens.COMMA), false, null);
        this.padded = new Padded(expect(Tokens.COMMA));
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        padded.parse(builder);
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        padded.computeNextTokens(myNextTokens);
    }
}
