package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.*;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.SepBy.*;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.psi.Tokens.LPAREN;
import static mkolaczek.elm.psi.Tokens.RPAREN;

class Basic {

    public static Parser listing(String name, Parser listedValue) {
        return sequence(
                expect(LPAREN),
                listingContent(name + " content", listedValue),
                expect(RPAREN)
        )
         .as(Elements.MODULE_VALUE_LIST);
    }

    private static Parser listingContent(String name, Parser listedValue) {
        return or(name,
                expect(Tokens.OPEN_LISTING).as(Elements.OPEN_LISTING_NODE),
                commaSep(listedValue)
        );
    }

    public static Parser operator(Element operatorSymbol) {
        return sequence(
                expect(Tokens.LPAREN),
                operatorSymbol(operatorSymbol),
                expect(Tokens.RPAREN)
        ).as(Elements.OPERATOR);
    }

    public static Parser operatorSymbol(Element as) {
        return or("operator symbol",
                expect(Tokens.RUNE_OF_AUTOCOMPLETION),
                expect(Tokens.SYM_OP),
                expect(Tokens.CONS),
                expect(Tokens.MINUS)
        ).as(as);
    }

    private static Parser squareBrackets(String name, Parser contents) {
        return surround(name, Tokens.LSQUAREBRACKET, Tokens.RSQUAREBRACKET, contents);
    }

    public static Parser parens(String name, Parser contents) {
        return surround(name, Tokens.LPAREN, Tokens.RPAREN, contents);
    }

    public static Parser brackets(Parser contents) {
        return surround(Tokens.LBRACKET, Tokens.RBRACKET, contents);
    }

    private static Parser brackets(String name, Parser contents) {
        return surround(name, Tokens.LBRACKET, Tokens.RBRACKET, contents);
    }

    private static Parser surround(String name, Token left, Token right, Parser contents) {
        return sequence(name, surroundContent(left, right, contents));
    }

    private static Parser surround(Token left, Token right, Parser contents) {
        return sequence(surroundContent(left, right, contents));
    }

    @NotNull
    private static Parser[] surroundContent(Token left, Token right, Parser contents) {
        return new Parser[]{expect(left), contents, expect(right)};
    }

    public static Parser docComment() {
        return sequence(
                expect(Tokens.BEGIN_DOC_COMMENT),
                expect(Tokens.END_DOC_COMMENT)
        ).as(Elements.DOC_COMMENT);
    }

    public static Parser spacePrefix(Parser parser) {
        return Many.many(String.format("space prefixed list of %ss", parser.name()),
                Try.tryP(parser)
        );
    }

    public static Parser tuple(String name, Parser expression) {
        return parens(name,
                tryP(commaSep(expression).as(Elements.SURROUND_CONTENTS))
        );
    }

    @NotNull
    static Parser list(String name, ParserBox expression) {
        return squareBrackets(name,
                tryP(commaSep(expression).as(Elements.SURROUND_CONTENTS))
        );
    }

    @NotNull
    static Parser record(String name, Parser fieldSuffix) {
        return brackets(name,
                tryP(
                        sequence(
                                expect(Tokens.LOW_VAR),
                                or(
                                        sequence(
                                                expect(Tokens.PIPE),
                                                tryCommaSep(recordField(fieldSuffix))
                                        ),
                                        sequence(
                                                fieldSuffix,
                                                commaSepSuffix(recordField(fieldSuffix))
                                        )
                                )
                        ).as(Elements.SURROUND_CONTENTS)
                )
        );
    }

    @NotNull
    private static Sequence recordField(Parser fieldSuffix) {
        return Sequence.sequence("record field",
                expect(Tokens.LOW_VAR),
                fieldSuffix
        );
    }
}
