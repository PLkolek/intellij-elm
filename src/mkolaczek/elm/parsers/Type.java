package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.core.DottedVar.qualifiedCapVar;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;

class Type {

    public static final ParserBox expression = new ParserBox("type expression");

    @NotNull
    private static Sequence fieldSuffix() {
        return sequence("record field suffix",
                annotationEnd()
        );
    }


    private static final Parser term =
            or("type term",
                    typeRef(),
                    expect(Tokens.LOW_VAR).as(Elements.TYPE_VARIABLE),
                    tuple("tuple type", expression),
                    record("record type", fieldSuffix())
            );

    private static final Parser app =
            sequence("type application",
                    typeRef(),
                    spacePrefix(term)
            );

    private static Parser typeRef() {
        return qualifiedCapVar("type name",
                Elements.TYPE_NAME_REF
        ).as(Elements.QUALIFIED_TYPE_NAME_REF);
    }

    private static final Parser expression2 =
            sequence("type expression",
                    or(app, term),
                    tryP(
                            sequence(
                                    maybeWhitespace(expect(Tokens.ARROW)),
                                    maybeWhitespace(expression)
                            )
                    )
            ).as(Elements.TYPE_EXPRESSION);

    public static Parser unionConstructor() {
        return sequence("union constructor",
                expect(Tokens.CAP_VAR).as(Elements.TYPE_CONSTRUCTOR_NAME),
                spacePrefix(term).as(Elements.TYPE_CONSTRUCTOR_ARGS)
        ).as(Elements.TYPE_CONSTRUCTOR);
    }

    static {
        expression.setParser(expression2);
    }

    public static Parser annotationEnd() {
        return sequence(
                maybeWhitespace(expect(Tokens.COLON)),
                maybeWhitespace(Type.expression)
        ).as(Elements.TYPE_ANNOTATION_END);
    }
}
