package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.SepBy.commaSepSuffix;
import static mkolaczek.elm.parsers.SepBy.tryCommaSep;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;

public class
Type {

    public static ParserBox expression = new ParserBox("type expression");

    private static Parser tuple =
            parens("tuple type", tryP(tryCommaSep(expression)));

    private static Parser field =
            sequence("record field",
                    expect(Tokens.LOW_VAR),
                    fieldSuffix()
            );

    @NotNull
    private static Sequence fieldSuffix() {
        return sequence("record field suffix",
                maybeWhitespace(),
                expect(Tokens.COLON),
                maybeWhitespace(),
                expression
        );
    }


    private static Parser record =
            brackets("record type",
                    tryP(
                            sequence(
                                    expect(Tokens.LOW_VAR),
                                    maybeWhitespace(),
                                    or(
                                            sequence(
                                                    expect(Tokens.PIPE),
                                                    maybeWhitespace(),
                                                    tryCommaSep(field)
                                            ),
                                            sequence(
                                                    fieldSuffix(),
                                                    maybeWhitespace(),
                                                    commaSepSuffix(field))
                                    )
                            )
                    )
            );

    private static Parser term =
            or("type term",
                    typeRef(),
                    expect(Tokens.LOW_VAR),
                    tuple,
                    record
            );

    private static Parser app =
            sequence("type application",
                    typeRef(),
                    spacePrefix(term)
            );

    private static Parser typeRef() {
        return or(
                dottedCapVar("type name"),
                expect(Tokens.RUNE_OF_AUTOCOMPLETION)
        ).as(Elements.TYPE_NAME_REF);
    }

    private static Parser expression2 =
            sequence("type expression",
                    or(
                            app,
                            term
                    ),
                    tryP(
                            sequence(
                                    maybeWhitespace(),
                                    expect(Tokens.ARROW),
                                    maybeWhitespace(),
                                    expression
                            )
                    )
            );

    public static Parser unionConstructor() {
        return sequence("union constructor",
                expect(Tokens.CAP_VAR).as(Elements.TYPE_CONSTRUCTOR_NAME),
                spacePrefix(term)
        ).as(Elements.TYPE_CONSTRUCTOR);
    }

    static {

        expression.setParser(expression2);
    }
}
