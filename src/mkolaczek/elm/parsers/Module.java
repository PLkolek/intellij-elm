package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.parsers.core.WhiteSpace;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.SepBy.tryCommaSep;
import static mkolaczek.elm.parsers.core.DottedCapVar.dottedCapVar;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Many.many;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.psi.Elements.MODULE_ALIAS;
import static mkolaczek.elm.psi.Tokens.CAP_VAR;
import static mkolaczek.elm.psi.Tokens.RUNE_OF_AUTOCOMPLETION;

public class Module {

    public static Parser moduleHeader() {
        return sequence("Module Header",
                WhiteSpace.freshLine(),
                tryP(moduleDeclaration()),
                tryP(sequence("Doc comment", Basic.docComment(), WhiteSpace.freshLine())),
                many(Module.importLine()).as(Elements.IMPORTS)
        );
    }

    public static Parser importLine() {
        return sequence(
                expect(Tokens.IMPORT),
                WhiteSpace.maybeWhitespace(),
                or(
                        dottedCapVar("module name").as(Elements.MODULE_NAME_REF)
                ),
                tryP(
                        sequence("as clause",
                                WhiteSpace.maybeWhitespace(),
                                expect(Tokens.AS),
                                WhiteSpace.maybeWhitespace(),
                                expect(CAP_VAR).as(MODULE_ALIAS)
                        )
                ),
                tryP(
                        sequence("exposing clause",
                                WhiteSpace.maybeWhitespace(),
                                exposing()
                        )
                ),
                WhiteSpace.freshLine()
        ).as(Elements.IMPORT_LINE);
    }

    public static Parser exposing() {
        return sequence(
                expect(Tokens.EXPOSING),
                WhiteSpace.maybeWhitespace(),
                Basic.listing("list of exposed values", exportValue())
        ).as(Elements.EXPOSING_NODE);

    }

    private static Parser exportValue() {
        return or(
                expect(Tokens.RUNE_OF_AUTOCOMPLETION).as(Elements.RUNE_OF_AUTOCOMPLETION_EL),
                expect(Tokens.LOW_VAR).as(Elements.VALUE_EXPORT),
                Basic.operator(),
                typeExport()
        ).as(Elements.EXPORTED_VALUE);
    }

    private static Parser typeExport() {
        return sequence("exported type",
                expect(CAP_VAR).as(Elements.TYPE_NAME_REF),
                tryP(
                        Basic.listing("type constructors",
                                or(
                                        expect(CAP_VAR),
                                        expect(RUNE_OF_AUTOCOMPLETION)
                                ).as(Elements.TYPE_CONSTRUCTOR_REF)
                        )
                )
        ).as(Elements.TYPE_EXPORT);
    }


    public static Parser settings() {
        return sequence(
                expect(Tokens.WHERE),
                WhiteSpace.maybeWhitespace(),
                settingsList()
        ).as(Elements.EFFECT_MODULE_SETTINGS);
    }

    private static Parser settingsList() {
        return Basic.brackets(
                tryCommaSep(
                        sequence(
                                expect(Tokens.LOW_VAR),
                                Basic.padded(expect(Tokens.EQUALS)),
                                expect(Tokens.CAP_VAR)
                        ).as(Elements.EFFECT_MODULE_SETTING)
                )
        ).as(Elements.EFFECT_MODULE_SETTINGS_LIST);
    }

    static Parser moduleDeclaration() {
        Sequence effectSequence =
                sequence("Module declaration",
                        expect(Tokens.EFFECT),
                        WhiteSpace.maybeWhitespace(),
                        expect(Tokens.MODULE),
                        WhiteSpace.maybeWhitespace(),
                        dottedCapVar("module name").as(Elements.MODULE_NAME),
                        WhiteSpace.maybeWhitespace(),
                        settings(),
                        WhiteSpace.maybeWhitespace(),
                        exposing(),
                        WhiteSpace.freshLine()
                );

        Sequence sequence =
                sequence("Module declaration",
                        or("Module declaration keywords",
                                sequence("Port module declaration keywords",
                                        expect(Tokens.PORT),
                                        WhiteSpace.maybeWhitespace(),
                                        expect(Tokens.MODULE)
                                ),
                                expect(Tokens.MODULE)
                        ),
                        WhiteSpace.maybeWhitespace(),
                        dottedCapVar("module name").as(Elements.MODULE_NAME),
                        WhiteSpace.maybeWhitespace(),
                        exposing(),
                        WhiteSpace.freshLine()
                );
        return or(effectSequence, sequence).as(Elements.MODULE_HEADER);
    }
}
