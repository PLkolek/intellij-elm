package mkolaczek.elm.parsers.faultTolerant;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class ListingValues extends FTParserAbstr {
    private final Sequence sequence;

    public ListingValues(FTParser listedValue) {
        super("listing values", listedValue.startingTokens(), false, null);
        this.sequence = new Sequence("listing values",
                listedValue,
                new Many("more listing values",
                        new Sequence("more listing values",
                                new PaddedComma(),
                                listedValue
                        )
                )
        );
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
