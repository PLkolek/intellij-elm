package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.Expect.expectAs;
import static mkolaczek.elm.parsers.faultTolerant.Many.many;
import static mkolaczek.elm.parsers.faultTolerant.Or.or;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequence;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequenceAs;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.noWhitespace;
import static mkolaczek.elm.psi.Tokens.LPAREN;
import static mkolaczek.elm.psi.Tokens.RPAREN;

public class FTBasic {

    public static FTParser listing(String name, FTParser listedValue) {
        return sequenceAs(Elements.MODULE_VALUE_LIST,
                expect(LPAREN),
                listingContent(name + " content", listedValue),
                expect(RPAREN)
        ).separatedBy(maybeWhitespace());
    }

    public static FTParser listingContent(String name, FTParser listedValue) {
        return or(name,
                expectAs(Tokens.OPEN_LISTING, Elements.OPEN_LISTING_NODE),
                listingValues(listedValue)
        );
    }

    public static FTParser listingValues(FTParser listedValue) {
        return sequence("listing values",
                listedValue,
                many("more listing values",
                        paddedComma(),
                        listedValue
                )
        );
    }

    private static FTParser paddedComma() {
        return padded(Tokens.COMMA);
    }

    public static FTParser padded(Token paddedToken) {
        return padded(expect(paddedToken));
    }

    public static FTParser padded(FTParser paddedValue) {
        return sequence(paddedValue.name(),
                maybeWhitespace(),
                paddedValue,
                maybeWhitespace()
        );
    }

    public static FTParser dottedCapVar(Element as) {
        return sequenceAs(as,
                expect(Tokens.CAP_VAR),
                many(as.getName() + " parts",
                        noWhitespace(),
                        expect(Tokens.DOT),
                        noWhitespace(),
                        expect(Tokens.CAP_VAR)
                )
        );
    }

    public static FTParser operator() {
        return sequenceAs(Elements.OPERATOR,
                expect(Tokens.LPAREN),
                operatorSymbol(),
                expect(Tokens.RPAREN)
        ).separatedBy(maybeWhitespace());
    }

    private static FTParser operatorSymbol() {
        return or("operator symbol", expect(Tokens.SYM_OP), expect(Tokens.COMMA_OP));
    }

    public static FTParser commaSep(FTParser parser) {
        return Try.tryP(
                sequence(String.format("comma separated list of %ss", parser.name()),
                        parser,
                        many(String.format("more %ss", parser.name()),
                                paddedComma(),
                                parser
                        )
                )
        );
    }

    public static FTParser bracketsAs(Element as, FTParser contents) {
        return surroundAs(as, Tokens.LBRACKET, Tokens.RBRACKET, contents);
    }

    public static FTParser surroundAs(Element as, Token left, Token right, FTParser contents) {
        return sequenceAs(as,
                expect(left),
                padded(contents),
                expect(right)
        );
    }

    public static FTParser docComment() {
        return sequenceAs(Elements.DOC_COMMENT,
                expect(Tokens.BEGIN_DOC_COMMENT),
                expect(Tokens.END_DOC_COMMENT)
        );
    }
}
