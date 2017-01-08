package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.FTBasic.listing;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequenceAs;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.maybeWhitespace;

public class FTModule {

    public static FTParser exposing() {
        return sequenceAs("exposing list", Elements.EXPOSING_NODE,
                expect(Tokens.EXPOSING),
                maybeWhitespace(),
                listing("list of exposed values", expect(Tokens.LOW_VAR)));

    }


}
