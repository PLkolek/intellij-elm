package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.psi.Elements;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.Literal.literal;
import static mkolaczek.elm.parsers.SepBy.tryCommaSep;
import static mkolaczek.elm.parsers.core.DottedVar.dottedCapVar;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.psi.Elements.MODULE_NAME_REF;
import static mkolaczek.elm.psi.Elements.TYPE_CONSTRUCTOR_REF;
import static mkolaczek.elm.psi.Tokens.*;

public class Pattern {

    public static final ParserBox expression = new ParserBox("pattern expression");

    public static Parser term() {
        return or(
                expect(RUNE_OF_AUTOCOMPLETION),
                record(),
                tuple("tuple pattern", expression),
                list("list pattern", expression),
                expect(UNDERSCORE),
                variable(),
                constructor(),
                literal()
        ).as(Elements.PATTERN_TERM);
    }

    @NotNull
    public static Parser variable() {
        return or("pattern variable", expect(LOW_VAR), expect(RUNE_OF_AUTOCOMPLETION)).as(Elements.VALUE_NAME);
    }

    private static Parser record() {
        return brackets(tryCommaSep(variable()));
    }

    private static Parser consTerm() {
        return or(
                sequence(
                        constructor(),
                        spacePrefix(term())
                ),
                term()
        );
    }

    private static Parser constructor() {
        return dottedCapVar("constructor",
                MODULE_NAME_REF,
                TYPE_CONSTRUCTOR_REF).as(Elements.QUALIFIED_TYPE_CONSTRUCTOR_REF);
    }

    static {
        expression.setParser(expr());
    }

    @NotNull
    private static Sequence expr() {
        return sequence(
                consTerm(),
                tryP(
                        maybeWhitespace(or(cons(), as()))
                )
        );
    }

    @NotNull
    private static Sequence cons() {
        return sequence(
                expect(CONS),
                maybeWhitespace(consTerm())
        );
    }

    @NotNull
    private static Sequence as() {
        return sequence(
                expect(AS),
                maybeWhitespace(variable())
        );
    }
}
