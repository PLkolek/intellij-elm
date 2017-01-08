package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.Type.MAYBE;

public class Exposing extends FTParserAbstr {

    private final Sequence sequence;

    protected Exposing() {
        super("exposing list", Sets.newHashSet(Tokens.EXPOSING), false, Elements.EXPOSING_NODE);
        this.sequence = new Sequence("exposing list",
                new Expect("exposing", Tokens.EXPOSING),
                new WhiteSpace(MAYBE),
                new Listing("list of exposed values", new Expect("TODO", Tokens.LOW_VAR)));
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        sequence.parse(builder);
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        sequence.computeNextTokens(myNextTokens);
    }
}
