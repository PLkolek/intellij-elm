package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;

public class Type {

    public static ParserBox expression = new ParserBox("type expression");

    private static Parser tuple =
            parens("tuple type", tryP(commaSep(expression)));


    //TODO
    private static Parser term =
            or("type term",
                    Basic.dottedCapVar("type name"),
                    expect(Tokens.LOW_VAR),
                    tuple
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
