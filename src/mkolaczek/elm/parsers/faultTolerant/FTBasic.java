package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.Type.MAYBE;

public class FTBasic {

    public static FTParser listing(String name, FTParser listedValue) {
        return new Sequence(name,
                new WhiteSpace(MAYBE),
                new Expect(Tokens.LPAREN),
                new WhiteSpace(MAYBE),
                new ListingContent(name + " content", listedValue),
                new WhiteSpace(MAYBE),
                new Expect(Tokens.RPAREN)
        );
    }
}
