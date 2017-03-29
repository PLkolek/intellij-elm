package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.As;
import mkolaczek.elm.parsers.core.DottedVar;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.Basic.listing;
import static mkolaczek.elm.parsers.SepBy.tryCommaSep;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Many.many;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.parsers.core.WhiteSpace.freshLine;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.psi.Elements.MODULE_ALIAS;
import static mkolaczek.elm.psi.Elements.OPERATOR_SYMBOL_REF;
import static mkolaczek.elm.psi.Tokens.CAP_VAR;
import static mkolaczek.elm.psi.Tokens.RUNE_OF_AUTOCOMPLETION;

class Module {

    public static Parser moduleHeader() {
        return sequence("Module Header",
                tryP(freshLine(moduleDeclaration())),
                tryP(freshLine(Basic.docComment())),
                many(Module.importLine()).as(Elements.IMPORTS, As.Mode.MARK_ALWAYS)
        );
    }

    private static Parser importLine() {
        return freshLine(sequence(
                expect(Tokens.IMPORT),
                maybeWhitespace(DottedVar.moduleName().as(Elements.MODULE_NAME_REF)),
                tryP(
                        sequence("as clause",
                                maybeWhitespace(expect(Tokens.AS)).skipWsError(),
                                maybeWhitespace(expect(CAP_VAR).as(MODULE_ALIAS))
                        )
                ),
                tryP(maybeWhitespace(exposing()).skipWsError())
        ).as(Elements.IMPORT_LINE));
    }

    private static Parser exposing() {
        return sequence(
                expect(Tokens.EXPOSING),
                maybeWhitespace(listing("list of exposed values", exposed()))
        ).as(Elements.EXPOSING_NODE);

    }

    private static Parser exposed() {
        return or(
                expect(Tokens.LOW_VAR).as(Elements.VALUE_EXPOSING),
                Basic.operator(OPERATOR_SYMBOL_REF),
                exposedType()
        ).as(Elements.EXPOSED_VALUE);
    }

    private static Parser exposedType() {
        return sequence("exposed type",
                expect(CAP_VAR).as(Elements.TYPE_NAME_REF),
                tryP(
                        listing("type constructors",
                                or(
                                        expect(CAP_VAR),
                                        expect(RUNE_OF_AUTOCOMPLETION)
                                ).as(Elements.TYPE_CONSTRUCTOR_REF)
                        )
                )
        ).as(Elements.TYPE_EXPOSING);
    }


    private static Parser settings() {
        return sequence(
                expect(Tokens.WHERE),
                maybeWhitespace(settingsList())
        ).as(Elements.EFFECT_MODULE_SETTINGS);
    }

    private static Parser settingsList() {
        return Basic.brackets(
                tryCommaSep(
                        sequence(
                                expect(Tokens.LOW_VAR),
                                maybeWhitespace(expect(Tokens.EQUALS)),
                                maybeWhitespace(expect(Tokens.CAP_VAR))
                        ).as(Elements.EFFECT_MODULE_SETTING)
                )
        ).as(Elements.EFFECT_MODULE_SETTINGS_LIST);
    }

    private static Parser moduleDeclaration() {
        Sequence effectSequence =
                sequence("Module declaration",
                        expect(Tokens.EFFECT),
                        maybeWhitespace(expect(Tokens.MODULE)),
                        maybeWhitespace(DottedVar.moduleName().as(Elements.MODULE_NAME)),
                        maybeWhitespace(settings()).skipWsError(),
                        maybeWhitespace(exposing()).skipWsError()
                );

        Sequence sequence =
                sequence("Module declaration",
                        or("Module declaration keywords",
                                sequence("Port module declaration keywords",
                                        expect(Tokens.PORT),
                                        maybeWhitespace(expect(Tokens.MODULE)).skipWsError()
                                ),
                                expect(Tokens.MODULE)
                        ),
                        maybeWhitespace(DottedVar.moduleName().as(Elements.MODULE_NAME)),
                        maybeWhitespace(exposing()).skipWsError()
                );
        return or(effectSequence, sequence).as(Elements.MODULE_HEADER);
    }
}
