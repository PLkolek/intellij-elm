package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.FTBasic.*;
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


    public static FTParser settings() {
        return sequenceAs(Elements.EFFECT_MODULE_SETTINGS,
                expect(Tokens.WHERE),
                maybeWhitespace(),
                settingsList()
        );
    }

    private static FTParser settingsList() {
        return FTBasic.bracketsAs(Elements.EFFECT_MODULE_SETTINGS_LIST,
                commaSep(
                        sequenceAs(Elements.EFFECT_MODULE_SETTING,
                                expect(Tokens.LOW_VAR),
                                padded(expect(Tokens.EQUALS)),
                                expect(Tokens.CAP_VAR)
                        )
                )
        );
    }
}
