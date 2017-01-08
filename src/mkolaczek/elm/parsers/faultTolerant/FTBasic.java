package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.maybeWhitespace;

public class FTBasic {

    public static FTParser listing(String name, FTParser listedValue) {
        return new Sequence(name,
                maybeWhitespace(),
                expect(Tokens.LPAREN),
                maybeWhitespace(),
                new ListingContent(name + " content", listedValue),
                maybeWhitespace(),
                expect(Tokens.RPAREN)
        );
    }
}
