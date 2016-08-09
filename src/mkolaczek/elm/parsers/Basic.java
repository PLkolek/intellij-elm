package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmTypes;
import org.jetbrains.annotations.NotNull;

public class Basic {
    public static Parser listing(NamedParser listedValue) {
        return (PsiBuilder builder) -> {
            PsiBuilder.Marker m = builder.mark();
            Whitespace.maybeWhitespace(builder);
            if (builder.getTokenType() != ElmTypes.LPAREN) {
                m.rollbackTo();
                return true;
            }
            boolean success = Combinators.simpleSequence(builder,
                    Combinators.expect(ElmTypes.LPAREN),
                    Whitespace::maybeWhitespace,
                    listingContent(listedValue),
                    Whitespace::maybeWhitespace,
                    Combinators.expect(ElmTypes.RPAREN)
            );
            m.done(ElmTypes.MODULE_VALUE_LIST);
            return success;
        };
    }

    private static Parser listingContent(NamedParser listedValue) {
        return (PsiBuilder builder) ->
                Combinators.simpleOr(builder,
                        Combinators.expect(ElmTypes.OPEN_LISTING),
                        listingValues(listedValue));
    }

    private static NamedParser listingValues(NamedParser listedValue) {
        Parser parser = builder -> listedValue.apply(builder) && Combinators.simpleMany(builder, paddedComma(), listedValue);
        return NamedParser.of(listedValue.name(), parser);
    }

    public static Parser paddedComma() {
        return padded(ElmTypes.COMMA);
    }

    public static Parser paddedEquals() {
        return padded(ElmTypes.EQUALS);
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

    public static NamedParser operator() {
        Parser p = Combinators.sequence(
                Combinators.expect(ElmTypes.LPAREN),
                Whitespace::maybeWhitespace,
                Basic::operatorSymbol,
                Whitespace::maybeWhitespace,
                Combinators.expect(ElmTypes.RPAREN));
        return NamedParser.of("Operator", p);
    }

    private static boolean operatorSymbol(PsiBuilder builder) {
        return Combinators.simpleOr(builder, ElmTypes.SYM_OP, ElmTypes.COMMA_OP);
    }

    private static boolean dottedCapVar(@NotNull PsiBuilder builder, @NotNull IElementType type) {
        PsiBuilder.Marker m = builder.mark();
        boolean success = Combinators.simpleSequence(builder,
                Combinators.expect(ElmTypes.CAP_VAR),
                Combinators.simpleMany(ElmTypes.DOT, ElmTypes.CAP_VAR)
        );
        m.done(type);
        return success;
    }

    public static boolean dottedCapVar(@NotNull PsiBuilder builder) {
        return dottedCapVar(builder, ElmTypes.DOTTED_CAP_VAR);
    }

    public static Parser dottedCapVar(@NotNull IElementType type) {
        return builder -> dottedCapVar(builder, type);
    }

    public static NamedParser dottedCapVar() {
        return NamedParser.of("Dotted cap var", Basic::dottedCapVar);
    }
}
