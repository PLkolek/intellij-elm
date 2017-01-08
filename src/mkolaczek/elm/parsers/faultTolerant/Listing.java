package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.Type.MAYBE;

public class Listing extends FTParserAbstr {

    private final Sequence sequence;

    public Listing(String name, FTParser listedValue) {
        super(name, Sets.newHashSet(Tokens.LPAREN), false, Elements.MODULE_VALUE_LIST);
        this.sequence = new Sequence(name,
                new WhiteSpace(MAYBE),
                new Expect("(", Tokens.LPAREN),
                new WhiteSpace(MAYBE),
                new ListingContent(name + " content", listedValue),
                new WhiteSpace(MAYBE),
                new Expect(")", Tokens.RPAREN)
        );
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        this.sequence.parse(builder);
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        this.sequence.computeNextTokens(myNextTokens);
    }
}
