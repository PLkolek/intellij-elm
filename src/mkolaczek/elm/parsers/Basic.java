package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.*;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Sequence.sequenceAs;
import static mkolaczek.elm.psi.Tokens.LPAREN;
import static mkolaczek.elm.psi.Tokens.RPAREN;

public class Basic {

    public static Parser listing(String name, Parser listedValue) {
        return sequenceAs(Elements.MODULE_VALUE_LIST,
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
        return sequence("listing values",
                listedValue,
                commaSepSuffix(listedValue)
        );
    }

    private static Parser paddedComma() {
        return padded(Tokens.COMMA);
    }

    public static Parser padded(Token paddedToken) {
        return padded(Expect.expect(paddedToken));
    }

    public static Parser padded(Parser paddedValue) {
        return sequence(paddedValue.name(),
                WhiteSpace.maybeWhitespace(),
                paddedValue,
                WhiteSpace.maybeWhitespace()
        );
    }

    public static Parser dottedCapVar(String name) {
        return sequence(name, dottedCapVarBody(name)
        );
    }

    public static Parser dottedCapVar(Element as) {
        return sequenceAs(as, dottedCapVarBody(as.getName())
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
        return sequenceAs(Elements.OPERATOR,
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
                sequence(String.format("comma separated list of %ss", parser.name()),
                        parser,
                        commaSepSuffix(parser)
                )
        );
    }

    @NotNull
    public static Many commaSepSuffix(Parser parser) {
        return Many.many(String.format("more %ss", parser.name()),
                paddedComma(),
                parser
        );
    }

    public static Parser parens(String name, Parser contents) {
        return surround(name, Tokens.LPAREN, Tokens.RPAREN, contents);
    }

    public static Parser bracketsAs(Element as, Parser contents) {
        return surroundAs(as, Tokens.LBRACKET, Tokens.RBRACKET, contents);
    }

    public static Parser brackets(String name, Parser contents) {
        return surround(name, Tokens.LBRACKET, Tokens.RBRACKET, contents);
    }

    public static Parser surround(String name, Token left, Token right, Parser contents) {
        return sequence(name, surroundContent(left, right, contents));
    }

    @NotNull
    private static Parser[] surroundContent(Token left, Token right, Parser contents) {
        return new Parser[]{Expect.expect(left), padded(contents), Expect.expect(right)};
    }

    public static Parser surroundAs(Element as, Token left, Token right, Parser contents) {
        return sequenceAs(as, surroundContent(left, right, contents));
    }

    public static Parser docComment() {
        return sequenceAs(Elements.DOC_COMMENT,
                Expect.expect(Tokens.BEGIN_DOC_COMMENT),
                Expect.expect(Tokens.END_DOC_COMMENT)
        );
    }

    public static Parser spacePrefix(Parser parser) {
        return Many.many(String.format("space prefixed list of %ss", parser.name()),
                Try.tryP(
                        sequence("space prefixed " + parser.name(),
                                WhiteSpace.forcedWhitespace(),
                                parser)
                )
        );
    }
}
