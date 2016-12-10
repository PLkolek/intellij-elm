package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

public class Basic {

    public static Parser empty() {
        return builder -> true;
    }

    public static Parser listing(NamedParser listedValue) {
        return (PsiBuilder builder) -> {
            PsiBuilder.Marker m = builder.mark();
            Whitespace.maybeWhitespace(builder);
            if (builder.getTokenType() != Tokens.LPAREN) {
                m.rollbackTo();
                return true;
            }
            boolean success = Combinators.simpleSequence(builder,
                    Combinators.expect(Tokens.LPAREN),
                    Whitespace::maybeWhitespace,
                    listingContent(listedValue),
                    Whitespace::maybeWhitespace,
                    Combinators.expect(Tokens.RPAREN)
            );
            if (!success) {
                OnError.consumeUntil(builder, Tokens.RPAREN);
            }
            m.done(Elements.MODULE_VALUE_LIST);
            return true;
        };
    }

    private static Parser listingContent(NamedParser listedValue) {
        return (PsiBuilder builder) ->
                Combinators.simpleOr(builder,
                        Combinators.expectAs(Elements.OPEN_LISTING_NODE, Tokens.OPEN_LISTING),
                        listingValues(listedValue));
    }

    private static NamedParser listingValues(NamedParser listedValue) {
        Parser parser = builder -> listedValue.apply(builder) && Combinators.simpleMany(builder,
                paddedComma(),
                listedValue);
        return NamedParser.of(listedValue.name(), parser);
    }

    public static Parser paddedComma() {
        return padded(Tokens.COMMA);
    }

    public static Parser paddedEquals() {
        return padded(Tokens.EQUALS);
    }

    @NotNull
    public static Parser padded(IElementType token) {
        return padded(Combinators.expect(token));
    }

    public static Parser padded(Parser parser) {
        return (builder) -> {
            PsiBuilder.Marker marker = builder.mark();
            Whitespace.maybeWhitespace(builder);
            builder.getCurrentOffset();
            if (!parser.apply(builder)) {
                marker.rollbackTo();
                return false;
            }
            marker.drop();
            return Whitespace.maybeWhitespace(builder);
        };
    }

    public static Parser parens(Parser parser) {
        return surround(Tokens.LPAREN, Tokens.RPAREN, parser);
    }

    public static NamedParser operator() {
        Parser p = Combinators.sequenceAs(Elements.OPERATOR,
                Combinators.expect(Tokens.LPAREN),
                Whitespace::maybeWhitespace,
                Basic::operatorSymbol,
                Whitespace::maybeWhitespace,
                Combinators.expect(Tokens.RPAREN));
        return NamedParser.of("Operator", p);
    }

    private static boolean operatorSymbol(PsiBuilder builder) {
        return Combinators.simpleOr(builder, Tokens.SYM_OP, Tokens.COMMA_OP);
    }

    private static boolean dottedCapVar(@NotNull PsiBuilder builder, @NotNull IElementType type) {
        if (builder.getTokenType() != Tokens.CAP_VAR) {
            return false;
        }
        PsiBuilder.Marker m = builder.mark();
        Combinators.simpleSequence(builder,
                Combinators.expect(Tokens.CAP_VAR),
                Combinators.many(Whitespace::noWhitespace,
                        Combinators.expect(Tokens.DOT),
                        Whitespace::noWhitespace,
                        Combinators.expect(Tokens.CAP_VAR))
        );
        m.done(type);
        return true;
    }

    public static boolean dottedCapVar(@NotNull PsiBuilder builder) {
        return dottedCapVar(builder, Elements.DOTTED_CAP_VAR);
    }

    public static NamedParser dottedCapVar(@NotNull IElementType type) {
        return NamedParser.of(type.toString(), builder -> dottedCapVar(builder, type));
    }

    public static NamedParser dottedCapVar() {
        return NamedParser.of("Dotted cap var", Basic::dottedCapVar);
    }

    public static Parser brackets(Parser contents) {
        return surround(Tokens.LBRACKET, Tokens.RBRACKET, contents);
    }

    public static Parser surround(IElementType left, IElementType right, Parser contents) {
        return Combinators.sequence(
                Combinators.expect(left),
                Basic.padded(contents),
                Combinators.expect(right)
        );
    }

    public static Parser commaSep(Parser parser) {
        return builder -> {
            PsiBuilder.Marker marker = builder.mark();
            if (!parser.apply(builder)) {
                marker.rollbackTo();
                return true;
            }
            marker.drop();
            return Combinators.simpleMany(builder, paddedComma(), parser);
        };
    }
}
