package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.ElmTypes;
import org.jetbrains.annotations.NotNull;

public class Module {
    public static void module(@NotNull PsiBuilder builder) {
        if (builder.getTokenType() == ElmTypes.MODULE) {
            moduleDeclaration(builder);
        }
        if (builder.getTokenType() == ElmTypes.BEGIN_DOC_COMMENT) {
            Comment.docComment().apply(builder);
            Whitespace.freshLine(builder);
        }
        Combinators.simpleMany(builder, Module::importLine);
    }

    private static boolean importLine(PsiBuilder builder) {
        if (builder.getTokenType() != ElmTypes.IMPORT) {
            return false;
        }
        PsiBuilder.Marker m = builder.mark();
        boolean result = Combinators.simpleSequence(builder,
                Combinators.expect(ElmTypes.IMPORT),
                Whitespace::maybeWhitespace,
                Basic.dottedCapVar(ElmTypes.MODULE_NAME_REF)
        );
        if (!result) {
            OnError.consumeUntil(builder, ElmTypes.NEW_LINE);
            m.done(ElmTypes.IMPORT_2);
            return false;
        }
        PsiBuilder.Marker m2 = builder.mark();
        boolean hasAs = Combinators.simpleSequence(builder, Whitespace::maybeWhitespace, Combinators.expect(ElmTypes.AS));
        if (hasAs) {
            m2.drop();
            if (!Combinators.simpleSequence(builder, Whitespace::maybeWhitespace, Combinators.expectAs(ElmTypes.MODULE_ALIAS, ElmTypes.CAP_VAR))) {
                OnError.consumeUntil(builder, ElmTypes.NEW_LINE);
                m.done(ElmTypes.IMPORT_2);
                return false;
            }
        } else {
            m2.rollbackTo();
        }
        m2 = builder.mark();
        boolean hasExposing = Combinators.simpleSequence(builder, Whitespace::maybeWhitespace, Combinators.expect(ElmTypes.EXPOSING));
        if (hasExposing) {
            m2.drop();
            if (!Combinators.simpleSequence(builder, Whitespace::maybeWhitespace, Basic.listing(Module.exportValue()))) {
                OnError.consumeUntil(builder, ElmTypes.NEW_LINE);
                m.done(ElmTypes.IMPORT_2);
                return false;
            }
        } else {
            m2.rollbackTo();
        }
        boolean success = Whitespace.freshLine(builder);
        m.done(ElmTypes.IMPORT_2);
        return success;
    }

    private static void moduleDeclaration(@NotNull PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        boolean success = Combinators.simpleSequence(builder,
                Combinators.expect(ElmTypes.MODULE),
                Whitespace::maybeWhitespace,
                Basic.dottedCapVar(ElmTypes.MODULE_NAME),
                Whitespace::maybeWhitespace,
                Combinators.expect(ElmTypes.EXPOSING),
                Whitespace::maybeWhitespace,
                Basic.listing(Module.exportValue()),
                Whitespace::freshLine
        );
        if (!success) {
            OnError.consumeUntil(builder, ElmTypes.NEW_LINE);
        }
        marker.done(ElmTypes.MODULE_HEADER);
    }

    private static NamedParser exportValue() {
        Parser parser = Combinators.or(Combinators.expect(ElmTypes.LOW_VAR),
                Basic.operator(),
                Module.typeExport());
        return NamedParser.of("Exported value", parser);
    }

    private static NamedParser typeExport() {
        Parser p = Combinators.sequence(
                Combinators.expect(ElmTypes.CAP_VAR),
                Basic.listing(Combinators.expect(ElmTypes.CAP_VAR)));
        return NamedParser.of("Type export", p);
    }
}
