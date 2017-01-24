package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;

public class Type {

    public static ParserBox expression = new ParserBox("type expression");

    private static Parser tuple =
            parens("tuple type", tryP(commaSep(expression)));

    private static Parser field =
            sequence("record field",
                    expect(Tokens.LOW_VAR),
                    maybeWhitespace(),
                    expect(Tokens.COLON),
                    maybeWhitespace(),
                    expression
            );


    private static Parser record =
            brackets("record type",
                    //TODO
                    commaSep(field)
            );

    //TODO
    private static Parser term =
            or("type term",
                    Basic.dottedCapVar("type name"),
                    expect(Tokens.LOW_VAR),
                    tuple,
                    record
            );

    private static Parser app =
            sequence("type application",
                    Basic.dottedCapVar("type name"),
                    spacePrefix(term)
            );

    //TODO
    static {
        expression.setParser(app);
    }

}
