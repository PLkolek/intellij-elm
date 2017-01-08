package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expectAs;

public class ListingContent extends FTParserAbstr {
    private final Or or;

    public ListingContent(String name, FTParser listedValue) {
        super(name, Sets.union(listedValue.startingTokens(), Sets.newHashSet(Tokens.OPEN_LISTING)), false, null);
        this.or = new Or(name,
                expectAs(Tokens.OPEN_LISTING, Elements.OPEN_LISTING_NODE),
                new ListingValues(listedValue)
        );
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        or.parse(builder);
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        or.computeNextTokens(myNextTokens);
    }
}
