package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.*;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.psi.Tokens.LPAREN;
import static mkolaczek.elm.psi.Tokens.RPAREN;

public class Basic {

    public static Parser listing(String name, Parser listedValue) {
        return Sequence.sequenceAs(Elements.MODULE_VALUE_LIST,
                Expect.expect(LPAREN),
                listingContent(name + " content", listedValue),
                Expect.expect(RPAREN)
        ).separatedBy(WhiteSpace.maybeWhitespace());
    }

    public static Parser listingContent(String name, Parser listedValue) {
        return Or.or(name,
                Expect.expectAs(Tokens.OPEN_LISTING, Elements.OPEN_LISTING_NODE),
                listingValues(listedValue)
        );
    }

    public static Parser listingValues(Parser listedValue) {
        return Sequence.sequence("listing values",
                listedValue,
                Many.many("more listing values",
                        paddedComma(),
                        listedValue
                )
        );
    }

    private static Parser paddedComma() {
        return padded(Tokens.COMMA);
    }

    public static Parser padded(Token paddedToken) {
        return padded(Expect.expect(paddedToken));
    }

    public static Parser padded(Parser paddedValue) {
        return Sequence.sequence(paddedValue.name(),
                WhiteSpace.maybeWhitespace(),
                paddedValue,
                WhiteSpace.maybeWhitespace()
        );
    }

    public static Parser dottedCapVar(String name) {
        return Sequence.sequence(name, dottedCapVarBody(name)
        );
    }

    public static Parser dottedCapVar(Element as) {
        return Sequence.sequenceAs(as, dottedCapVarBody(as.getName())
        );
    }

    @NotNull
    private static Parser[] dottedCapVarBody(String name) {
        return new Parser[]{Expect.expect(Tokens.CAP_VAR),
                Many.many(name + " parts",
                        WhiteSpace.noWhitespace(),
                        Expect.expect(Tokens.DOT),
                        WhiteSpace.noWhitespace(),
                        Expect.expect(Tokens.CAP_VAR)
                )};
    }

    public static Parser operator() {
        return Sequence.sequenceAs(Elements.OPERATOR,
                Expect.expect(Tokens.LPAREN),
                operatorSymbol(),
                Expect.expect(Tokens.RPAREN)
        ).separatedBy(WhiteSpace.maybeWhitespace());
    }

    private static Parser operatorSymbol() {
        return Or.or("operator symbol", Expect.expect(Tokens.SYM_OP), Expect.expect(Tokens.COMMA_OP));
    }

    public static Parser commaSep(Parser parser) {
        return Try.tryP(
                Sequence.sequence(String.format("comma separated list of %ss", parser.name()),
                        parser,
                        Many.many(String.format("more %ss", parser.name()),
                                paddedComma(),
                                parser
                        )
                )
        );
    }

    public static Parser bracketsAs(Element as, Parser contents) {
        return surroundAs(as, Tokens.LBRACKET, Tokens.RBRACKET, contents);
    }

    public static Parser surroundAs(Element as, Token left, Token right, Parser contents) {
        return Sequence.sequenceAs(as,
                Expect.expect(left),
                padded(contents),
                Expect.expect(right)
        );
    }

    public static Parser docComment() {
        return Sequence.sequenceAs(Elements.DOC_COMMENT,
                Expect.expect(Tokens.BEGIN_DOC_COMMENT),
                Expect.expect(Tokens.END_DOC_COMMENT)
        );
    }
}
