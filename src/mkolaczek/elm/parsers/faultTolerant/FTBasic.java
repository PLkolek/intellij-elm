package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.Expect.expectAs;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequenceAs;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.psi.Tokens.LPAREN;
import static mkolaczek.elm.psi.Tokens.RPAREN;

public class FTBasic {

    public static FTParser listing(String name, FTParser listedValue) {
        return sequenceAs(name, Elements.MODULE_VALUE_LIST,
                expect(LPAREN),
                listingContent(name + " content", listedValue),
                expect(RPAREN)
        ).separatedBy(maybeWhitespace());
    }

    public static FTParser listingContent(String name, FTParser listedValue) {
        return new Or(name,
                expectAs(Tokens.OPEN_LISTING, Elements.OPEN_LISTING_NODE),
                new ListingValues(listedValue)
        );
    }

}
