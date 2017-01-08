package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Elements;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequenceAs;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.psi.Tokens.LPAREN;
import static mkolaczek.elm.psi.Tokens.RPAREN;

public class FTBasic {

    public static FTParser listing(String name, FTParser listedValue) {
        return sequenceAs(name, Elements.MODULE_VALUE_LIST,
                expect(LPAREN),
                new ListingContent(name + " content", listedValue),
                expect(RPAREN)
        ).separatedBy(maybeWhitespace());
    }
}
