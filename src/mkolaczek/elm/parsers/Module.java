package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.*;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.psi.Elements.MODULE_ALIAS;
import static mkolaczek.elm.psi.Tokens.CAP_VAR;

public class Module {

    public static Parser moduleHeader() {
        return Sequence.sequence("Module Header",
                WhiteSpace.freshLine(),
                moduleDeclaration(),
                Try.tryP(Sequence.sequence("Doc comment", Basic.docComment(), WhiteSpace.freshLine())),
                Many.manyAs(Elements.IMPORTS, Module.importLine())
        );
    }

    public static Parser importLine() {
        return Sequence.sequenceAs(Elements.IMPORT_LINE,
                Expect.expect(Tokens.IMPORT),
                WhiteSpace.maybeWhitespace(),
                Basic.dottedCapVar(Elements.MODULE_NAME_REF),
                Try.tryP(
                        Sequence.sequence("as clause",
                                WhiteSpace.maybeWhitespace(),
                                Expect.expect(Tokens.AS),
                                WhiteSpace.maybeWhitespace(),
                                Expect.expectAs(CAP_VAR, MODULE_ALIAS)
                        )
                ),
                Try.tryP(
                        Sequence.sequence("exposing clause",
                                WhiteSpace.maybeWhitespace(),
                                exposing()
                        )
                ),
                WhiteSpace.freshLine()
        );
    }

    public static Parser exposing() {
        return Sequence.sequenceAs(Elements.EXPOSING_NODE,
                Expect.expect(Tokens.EXPOSING),
                WhiteSpace.maybeWhitespace(),
                Basic.listing("list of exposed values", exportValue()));

    }

    private static Parser exportValue() {
        return Or.orAs(Elements.EXPORTED_VALUE,
                Expect.expect(Tokens.LOW_VAR),
                Basic.operator(),
                typeExport()
        );
    }

    private static Parser typeExport() {
        return Sequence.sequence("exported type",
                Expect.expect(CAP_VAR),
                Try.tryP(
                        Basic.listing("type constructors", Expect.expect(CAP_VAR))
                )
        );
    }


    public static Parser settings() {
        return Sequence.sequenceAs(Elements.EFFECT_MODULE_SETTINGS,
                Expect.expect(Tokens.WHERE),
                WhiteSpace.maybeWhitespace(),
                settingsList()
        );
    }

    private static Parser settingsList() {
        return Basic.bracketsAs(Elements.EFFECT_MODULE_SETTINGS_LIST,
                Basic.commaSep(
                        Sequence.sequenceAs(Elements.EFFECT_MODULE_SETTING,
                                Expect.expect(Tokens.LOW_VAR),
                                Basic.padded(Expect.expect(Tokens.EQUALS)),
                                Expect.expect(Tokens.CAP_VAR)
                        )
                )
        );
    }

    static Parser moduleDeclaration() {
        Sequence effectSequence =

                Sequence.sequence("Module declaration",
                        Expect.expect(Tokens.EFFECT),
                        WhiteSpace.maybeWhitespace(),
                        Expect.expect(Tokens.MODULE),
                        WhiteSpace.maybeWhitespace(),
                        Basic.dottedCapVar(Elements.MODULE_NAME),
                        WhiteSpace.maybeWhitespace(),
                        settings(),
                        WhiteSpace.maybeWhitespace(),
                        exposing(),
                        WhiteSpace.freshLine()
                );

        Sequence sequence =
                Sequence.sequence("Module declaration",
                        Or.or("Module declaration keywords",
                                Sequence.sequence("Port module declaration keywords",
                                        Expect.expect(Tokens.PORT),
                                        WhiteSpace.maybeWhitespace(),
                                        Expect.expect(Tokens.MODULE)
                                ),
                                Expect.expect(Tokens.MODULE)
                        ),
                        WhiteSpace.maybeWhitespace(),
                        Basic.dottedCapVar(Elements.MODULE_NAME),
                        WhiteSpace.maybeWhitespace(),
                        exposing(),
                        WhiteSpace.freshLine()
                );
        return Try.tryP(Or.orAs(Elements.MODULE_HEADER, effectSequence, sequence));
    }
}
