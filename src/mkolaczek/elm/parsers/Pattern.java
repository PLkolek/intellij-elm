package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.psi.Elements;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.SepBy.commaSep;
import static mkolaczek.elm.parsers.SepBy.tryCommaSep;
import static mkolaczek.elm.parsers.core.DottedCapVar.dottedCapVar;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.psi.Tokens.*;

public class Pattern {

    public static ParserBox expression = new ParserBox("pattern expression");

    public static Parser term() {
        //TODO: continue
        return or(
                record(),
                tuple()
        );
    }

    private static Parser record() {
        return brackets(tryCommaSep(expect(LOW_VAR)));
    }

    private static Parser tuple() {
        return parens("tuple type",
                tryP(commaSep(expression).as(Elements.SURROUND_CONTENTS))
        );
    }

    private static Parser consTerm() {
        return or(
                sequence(
                        dottedCapVar("constructor"),
                        spacePrefix(term())
                ),
                term()
        );
    }

    static {
        expression.setParser(
                sequence(
                        consTerm(),
                        maybeWhitespace(),
                        tryP(
                                or(
                                        sequence(
                                                expect(CONS),
                                                maybeWhitespace(),
                                                consTerm()
                                        ),
                                        sequence(
                                                expect(AS),
                                                maybeWhitespace(),
                                                expect(LOW_VAR)
                                        )
                                )
                        )
                )
        );
    }
}
