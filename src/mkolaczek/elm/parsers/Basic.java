package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Many;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.Try;
import mkolaczek.elm.parsers.core.WhiteSpace;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.SepBy.commaSep;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.psi.Tokens.LPAREN;
import static mkolaczek.elm.psi.Tokens.RPAREN;

public class Basic {

    public static Parser listing(String name, Parser listedValue) {
        return sequence(
                expect(LPAREN),
                listingContent(name + " content", listedValue),
                expect(RPAREN)
        ).separatedBy(maybeWhitespace())
         .as(Elements.MODULE_VALUE_LIST);
    }

    public static Parser listingContent(String name, Parser listedValue) {
        return or(name,
                expect(Tokens.OPEN_LISTING).as(Elements.OPEN_LISTING_NODE),
                commaSep(listedValue)
        );
    }

    public static Parser padded(Token paddedToken) {
        return padded(expect(paddedToken));
    }

    public static Parser padded(Parser paddedValue) {
        return sequence(paddedValue.name(),
                maybeWhitespace(),
                paddedValue,
                maybeWhitespace()
        );
    }

    public static Parser operator() {
        return sequence(
                expect(Tokens.LPAREN),
                operatorSymbol(),
                expect(Tokens.RPAREN)
        ).separatedBy(maybeWhitespace()).as(Elements.OPERATOR);
    }

    public static Parser operatorSymbol() {
        return or("operator symbol", expect(Tokens.SYM_OP), expect(Tokens.COMMA_OP)).as(Elements.OPERATOR_SYMBOL);
    }

    public static Parser parens(String name, Parser contents) {
        return surround(name, Tokens.LPAREN, Tokens.RPAREN, contents);
    }

    public static Parser brackets(Parser contents) {
        return surround(Tokens.LBRACKET, Tokens.RBRACKET, contents);
    }

    public static Parser brackets(String name, Parser contents) {
        return surround(name, Tokens.LBRACKET, Tokens.RBRACKET, contents);
    }

    public static Parser surround(String name, Token left, Token right, Parser contents) {
        return sequence(name, surroundContent(left, right, contents));
    }

    @NotNull
    private static Parser[] surroundContent(Token left, Token right, Parser contents) {
        return new Parser[]{expect(left), maybeWhitespace(), contents, maybeWhitespace(), expect(right)};
    }

    public static Parser surround(Token left, Token right, Parser contents) {
        return sequence(surroundContent(left, right, contents));
    }

    public static Parser docComment() {
        return sequence(
                expect(Tokens.BEGIN_DOC_COMMENT),
                expect(Tokens.END_DOC_COMMENT)
        ).as(Elements.DOC_COMMENT);
    }

    public static Parser spacePrefix(Parser parser) {
        return Many.many(String.format("space prefixed list of %ss", parser.name()),
                Try.tryP(
                        sequence("space prefixed " + parser.name(),
                                WhiteSpace.maybeWhitespace(),
                                parser)
                )
        );
    }
}
