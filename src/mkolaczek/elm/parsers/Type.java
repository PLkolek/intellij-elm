package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.core.DottedVar.dottedCapVar;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;

public class Type {

    public static ParserBox expression = new ParserBox("type expression");

    @NotNull
    private static Sequence fieldSuffix() {
        return sequence("record field suffix",
                maybeWhitespace(expect(Tokens.COLON)),
                maybeWhitespace(expression)
        );
    }


    private static Parser term =
            or("type term",
                    typeRef(),
                    expect(Tokens.LOW_VAR),
                    tuple("tuple type", expression),
                    record("record type", fieldSuffix())
            );

    private static Parser app =
            sequence("type application",
                    typeRef(),
                    spacePrefix(term)
            );

    private static Parser typeRef() {
        return dottedCapVar("type name",
                Elements.MODULE_NAME_REF,
                Elements.TYPE_NAME_REF
        ).as(Elements.QUALIFIED_TYPE_NAME_REF);
    }

    private static Parser expression2 =
            sequence("type expression",
                    or(
                            app,
                            term
                    ),
                    tryP(
                            sequence(
                                    maybeWhitespace(expect(Tokens.ARROW)),
                                    maybeWhitespace(expression)
                            )
                    )
            );

    public static Parser unionConstructor() {
        return sequence("union constructor",
                expect(Tokens.CAP_VAR).as(Elements.TYPE_CONSTRUCTOR_NAME),
                spacePrefix(term).as(Elements.TYPE_CONSTRUCTOR_ARGS)
        ).as(Elements.TYPE_CONSTRUCTOR);
    }

    static {

        expression.setParser(expression2);
    }
}
