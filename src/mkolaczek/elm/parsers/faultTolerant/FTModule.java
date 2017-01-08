package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.FTBasic.listing;
import static mkolaczek.elm.parsers.faultTolerant.FTBasic.operator;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequence;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequenceAs;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.psi.Tokens.CAP_VAR;

public class FTModule {

    public static FTParser exposing() {
        return sequenceAs(Elements.EXPOSING_NODE,
                expect(Tokens.EXPOSING),
                maybeWhitespace(),
                listing("list of exposed values", exportValue()));

    }

    private static FTParser exportValue() {
        return Or.orAs(Elements.EXPORTED_VALUE,
                expect(Tokens.LOW_VAR),
                operator(),
                typeExport()
        );
    }

    private static FTParser typeExport() {
        return sequence("exported type",
                expect(CAP_VAR),
                listing("type constructors", expect(CAP_VAR))
        );
    }


}
