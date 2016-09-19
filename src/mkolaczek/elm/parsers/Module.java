package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.ElmTokenTypes;
import mkolaczek.elm.psi.ElmElementTypes;
import org.jetbrains.annotations.NotNull;

public class Module {
    public static void module(@NotNull PsiBuilder builder) {
        PsiBuilder.Marker module = builder.mark();
        if (builder.getTokenType() == ElmTokenTypes.MODULE) {
            moduleDeclaration(builder);
        }
        if (builder.getTokenType() == ElmTokenTypes.BEGIN_DOC_COMMENT) {
            Comment.docComment().apply(builder);
            Whitespace.freshLine(builder);
        }
        Combinators.simpleMany(builder, Module::importLine);
        module.done(ElmElementTypes.MODULE);
    }

    private static boolean importLine(PsiBuilder builder) {
        if (builder.getTokenType() != ElmTokenTypes.IMPORT) {
            return false;
        }
        PsiBuilder.Marker m = builder.mark();
        boolean result = Combinators.simpleSequence(builder,
                Combinators.expect(ElmTokenTypes.IMPORT),
                Whitespace::maybeWhitespace,
                Basic.dottedCapVar(ElmElementTypes.MODULE_NAME_REF)
        );
        if (!result) {
            OnError.consumeUntil(builder, ElmTokenTypes.NEW_LINE);
            m.done(ElmElementTypes.IMPORT_2);
            return false;
        }
        PsiBuilder.Marker m2 = builder.mark();
        boolean hasAs = Combinators.simpleSequence(builder, Whitespace::maybeWhitespace, Combinators.expect(ElmTokenTypes.AS));
        if (hasAs) {
            m2.drop();
            if (!Combinators.simpleSequence(builder, Whitespace::maybeWhitespace, Combinators.expectAs(ElmElementTypes.MODULE_ALIAS, ElmTokenTypes.CAP_VAR))) {
                OnError.consumeUntil(builder, ElmTokenTypes.NEW_LINE);
                m.done(ElmElementTypes.IMPORT_2);
                return false;
            }
        } else {
            m2.rollbackTo();
        }
        m2 = builder.mark();
        boolean hasExposing = Combinators.simpleSequence(builder, Whitespace::maybeWhitespace, Combinators.expect(ElmTokenTypes.EXPOSING));
        if (hasExposing) {
            m2.drop();
            if (!Combinators.simpleSequence(builder, Whitespace::maybeWhitespace, Basic.listing(Module.exportValue()))) {
                OnError.consumeUntil(builder, ElmTokenTypes.NEW_LINE);
                m.done(ElmElementTypes.IMPORT_2);
                return false;
            }
        } else {
            m2.rollbackTo();
        }
        boolean success = Whitespace.freshLine(builder);
        m.done(ElmElementTypes.IMPORT_2);
        return success;
    }

    private static void moduleDeclaration(@NotNull PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        boolean success = Combinators.simpleSequence(builder,
                Combinators.expect(ElmTokenTypes.MODULE),
                Whitespace::maybeWhitespace,
                Basic.dottedCapVar(ElmElementTypes.MODULE_NAME),
                Whitespace::maybeWhitespace,
                Combinators.expect(ElmTokenTypes.EXPOSING),
                Whitespace::maybeWhitespace,
                Basic.listing(Module.exportValue()),
                Whitespace::freshLine
        );
        if (!success) {
            OnError.consumeUntil(builder, ElmTokenTypes.NEW_LINE);
        }
        marker.done(ElmElementTypes.MODULE_HEADER);
    }

    private static NamedParser exportValue() {
        Parser parser = Combinators.or(Combinators.expect(ElmTokenTypes.LOW_VAR),
                Basic.operator(),
                Module.typeExport());
        return NamedParser.of("Exported value", parser);
    }

    private static NamedParser typeExport() {
        Parser p = Combinators.sequence(
                Combinators.expect(ElmTokenTypes.CAP_VAR),
                Basic.listing(Combinators.expect(ElmTokenTypes.CAP_VAR)));
        return NamedParser.of("Type export", p);
    }
}
