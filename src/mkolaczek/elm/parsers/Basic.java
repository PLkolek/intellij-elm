package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmElementTypes;
import mkolaczek.elm.psi.ElmTokenTypes;
import org.jetbrains.annotations.NotNull;

public class Basic {

    public static Parser empty() {
        return builder -> true;
    }

    public static Parser listing(NamedParser listedValue) {
        return (PsiBuilder builder) -> {
            PsiBuilder.Marker m = builder.mark();
            Whitespace.maybeWhitespace(builder);
            if (builder.getTokenType() != ElmTokenTypes.LPAREN) {
                m.rollbackTo();
                return true;
            }
            boolean success = Combinators.simpleSequence(builder,
                    Combinators.expect(ElmTokenTypes.LPAREN),
                    Whitespace::maybeWhitespace,
                    listingContent(listedValue),
                    Whitespace::maybeWhitespace,
                    Combinators.expect(ElmTokenTypes.RPAREN)
            );
            if (!success) {
                OnError.consumeUntil(builder, ElmTokenTypes.RPAREN);
            }
            m.done(ElmElementTypes.MODULE_VALUE_LIST);
            return true;
        };
    }

    private static Parser listingContent(NamedParser listedValue) {
        return (PsiBuilder builder) ->
                Combinators.simpleOr(builder,
                        Combinators.expectAs(ElmElementTypes.OPEN_LISTING_NODE, ElmTokenTypes.OPEN_LISTING),
                        listingValues(listedValue));
    }

    private static NamedParser listingValues(NamedParser listedValue) {
        Parser parser = builder -> listedValue.apply(builder) && Combinators.simpleMany(builder,
                paddedComma(),
                listedValue);
        return NamedParser.of(listedValue.name(), parser);
    }

    public static Parser paddedComma() {
        return padded(ElmTokenTypes.COMMA);
    }

    public static Parser paddedEquals() {
        return padded(ElmTokenTypes.EQUALS);
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
        return surround(ElmTokenTypes.LPAREN, ElmTokenTypes.RPAREN, parser);
    }

    public static NamedParser operator() {
        Parser p = Combinators.sequenceAs(ElmElementTypes.OPERATOR,
                Combinators.expect(ElmTokenTypes.LPAREN),
                Whitespace::maybeWhitespace,
                Basic::operatorSymbol,
                Whitespace::maybeWhitespace,
                Combinators.expect(ElmTokenTypes.RPAREN));
        return NamedParser.of("Operator", p);
    }

    private static boolean operatorSymbol(PsiBuilder builder) {
        return Combinators.simpleOr(builder, ElmTokenTypes.SYM_OP, ElmTokenTypes.COMMA_OP);
    }

    private static boolean dottedCapVar(@NotNull PsiBuilder builder, @NotNull IElementType type) {
        PsiBuilder.Marker m = builder.mark();
        boolean success = Combinators.simpleSequence(builder,
                Combinators.expect(ElmTokenTypes.CAP_VAR),
                Combinators.many(Whitespace::noWhitespace,
                        Combinators.expect(ElmTokenTypes.DOT),
                        Whitespace::noWhitespace,
                        Combinators.expect(ElmTokenTypes.CAP_VAR))
        );
        m.done(type);
        return success;
    }

    public static boolean dottedCapVar(@NotNull PsiBuilder builder) {
        return dottedCapVar(builder, ElmElementTypes.DOTTED_CAP_VAR);
    }

    public static Parser dottedCapVar(@NotNull IElementType type) {
        return builder -> dottedCapVar(builder, type);
    }

    public static NamedParser dottedCapVar() {
        return NamedParser.of("Dotted cap var", Basic::dottedCapVar);
    }

    public static Parser brackets(Parser contents) {
        return surround(ElmTokenTypes.LBRACKET, ElmTokenTypes.RBRACKET, contents);
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
