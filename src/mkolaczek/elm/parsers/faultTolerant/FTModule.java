package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.Expect.expectAs;
import static mkolaczek.elm.parsers.faultTolerant.FTBasic.*;
import static mkolaczek.elm.parsers.faultTolerant.Or.or;
import static mkolaczek.elm.parsers.faultTolerant.Or.orAs;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequence;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequenceAs;
import static mkolaczek.elm.parsers.faultTolerant.Try.tryP;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.freshLine;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.psi.Elements.MODULE_ALIAS;
import static mkolaczek.elm.psi.Tokens.CAP_VAR;

public class FTModule {

    public static FTParser importLine() {
        return sequenceAs(Elements.IMPORT_LINE,
                expect(Tokens.IMPORT),
                maybeWhitespace(),
                dottedCapVar(Elements.MODULE_NAME_REF),
                tryP(
                        sequence("as clause",
                                maybeWhitespace(),
                                expect(Tokens.AS),
                                maybeWhitespace(),
                                expectAs(CAP_VAR, MODULE_ALIAS)
                        )
                ),
                tryP(
                        sequence("exposing clause",
                                maybeWhitespace(),
                                exposing()
                        )
                ),
                freshLine()
        );
    }

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
                tryP(
                        listing("type constructors", expect(CAP_VAR))
                )
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

    static FTParser moduleDeclaration() {
        Sequence effectSequence =

                sequence("Module declaration",
                        expect(Tokens.EFFECT),
                        maybeWhitespace(),
                        expect(Tokens.MODULE),
                        maybeWhitespace(),
                        dottedCapVar(Elements.MODULE_NAME),
                        maybeWhitespace(),
                        settings(),
                        maybeWhitespace(),
                        exposing(),
                        freshLine()
                );

        Sequence sequence =
                sequence("Module declaration",
                        or("Module declaration keywords",
                                sequence("Port module declaration keywords",
                                        expect(Tokens.PORT),
                                        maybeWhitespace(),
                                        expect(Tokens.MODULE)
                                ),
                                expect(Tokens.MODULE)
                        ),
                        maybeWhitespace(),
                        dottedCapVar(Elements.MODULE_NAME),
                        maybeWhitespace(),
                        exposing(),
                        freshLine()
                );
        return tryP(orAs(Elements.MODULE_HEADER, effectSequence, sequence));
    }
}
