package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

public class Basic {

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

    public static NamedParser dottedCapVar() {
        return NamedParser.of("Dotted cap var", Basic::dottedCapVar);
    }

}
