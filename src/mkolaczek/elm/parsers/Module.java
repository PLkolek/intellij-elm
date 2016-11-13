package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmElementTypes;
import mkolaczek.elm.psi.ElmTokenTypes;
import org.jetbrains.annotations.NotNull;

public class Module {
    public static void module(@NotNull PsiBuilder builder) {
        int startingOffset = builder.getCurrentOffset();
        PsiBuilder.Marker module = builder.mark();
        Whitespace.freshLine(builder);
        IElementType token = builder.getTokenType();
        if (token == ElmTokenTypes.MODULE || token == ElmTokenTypes.PORT || token == ElmTokenTypes.EFFECT) {
            moduleDeclaration(builder);
        }
        if (builder.getTokenType() == ElmTokenTypes.BEGIN_DOC_COMMENT) {
            Comment.docComment().apply(builder);
            Whitespace.freshLine(builder);
        }
        Combinators.simpleManyAs(builder, ElmElementTypes.IMPORTS, Module::importLine);
        if (startingOffset != builder.getCurrentOffset()) {
            module.done(ElmElementTypes.MODULE);
        } else {
            module.drop();
        }
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
        boolean hasAs = Combinators.simpleSequence(builder,
                Whitespace::maybeWhitespace,
                Combinators.expect(ElmTokenTypes.AS));
        if (hasAs) {
            m2.drop();
            if (!Combinators.simpleSequence(builder,
                    Whitespace::maybeWhitespace,
                    Combinators.expectAs(ElmElementTypes.MODULE_ALIAS, ElmTokenTypes.CAP_VAR))) {
                OnError.consumeUntil(builder, ElmTokenTypes.NEW_LINE);
                m.done(ElmElementTypes.IMPORT_2);
                return false;
            }
        } else {
            m2.rollbackTo();
        }
        m2 = builder.mark();
        boolean hasExposing = Combinators.simpleSequence(builder,
                Whitespace::maybeWhitespace,
                Combinators.expect(ElmTokenTypes.EXPOSING));
        m2.rollbackTo();
        if (hasExposing) {
            Whitespace.maybeWhitespace(builder);

            if (!exposing().apply(builder)) {
                OnError.consumeUntil(builder, ElmTokenTypes.NEW_LINE);
                m.done(ElmElementTypes.IMPORT_2);
                return false;
            }
        }

        boolean success = Whitespace.freshLine(builder);
        m.done(ElmElementTypes.IMPORT_2);
        return success;
    }

    private static void moduleDeclaration(@NotNull PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        boolean success = Combinators.simpleSequence(builder,
                Combinators.or(
                        Combinators.expect(ElmTokenTypes.PORT, ElmTokenTypes.MODULE),
                        Combinators.expect(ElmTokenTypes.EFFECT, ElmTokenTypes.MODULE),
                        Combinators.expect(ElmTokenTypes.MODULE)),
                Whitespace::maybeWhitespace,
                Basic.dottedCapVar(ElmElementTypes.MODULE_NAME),
                Whitespace::maybeWhitespace,
                exposing(),
                Whitespace::freshLine
        );
        if (!success) {
            OnError.consumeUntil(builder, ElmTokenTypes.NEW_LINE);
        }
        marker.done(ElmElementTypes.MODULE_HEADER);
    }

    private static Parser exposing() {
        return Combinators.sequenceAs(ElmElementTypes.EXPOSING_NODE,
                Combinators.expect(ElmTokenTypes.EXPOSING),
                Whitespace::maybeWhitespace,
                Basic.listing(Module.exportValue()));
    }

    private static NamedParser exportValue() {
        Parser parser = Combinators.orAs(ElmElementTypes.EXPORTED_VALUE,
                Combinators.expect(ElmTokenTypes.LOW_VAR),
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
