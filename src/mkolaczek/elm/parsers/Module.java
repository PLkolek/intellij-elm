package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmElementTypes;
import mkolaczek.elm.psi.ElmTokenTypes;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.Combinators.*;
import static mkolaczek.elm.psi.ElmTokenTypes.CAP_VAR;

public class Module {
    public static void module(@NotNull PsiBuilder builder) {
        Whitespace.freshLine(builder);
        simpleTry(builder, Module::moduleDeclaration);
        if (builder.getTokenType() == ElmTokenTypes.BEGIN_DOC_COMMENT) {
            Comment.docComment().apply(builder);
            Whitespace.freshLine(builder);
        }
        Combinators.simpleManyAs(builder, ElmElementTypes.IMPORTS, Module::importLine);
    }

    private static boolean importLine(PsiBuilder builder) {
        if (builder.getTokenType() != ElmTokenTypes.IMPORT) {
            return false;
        }
        PsiBuilder.Marker m = builder.mark();
        boolean result = Combinators.simpleSequence(builder,
                expect(ElmTokenTypes.IMPORT),
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
                expect(ElmTokenTypes.AS));
        if (hasAs) {
            m2.drop();
            if (!Combinators.simpleSequence(builder,
                    Whitespace::maybeWhitespace,
                    Combinators.expectAs(ElmElementTypes.MODULE_ALIAS, CAP_VAR))) {
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
                expect(ElmTokenTypes.EXPOSING));
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

    private static boolean moduleDeclaration(@NotNull PsiBuilder builder) {
        IElementType token = builder.getTokenType();
        if (token != ElmTokenTypes.MODULE && token != ElmTokenTypes.PORT && token != ElmTokenTypes.EFFECT) {
            return false;
        }
        PsiBuilder.Marker marker = builder.mark();
        boolean isEffectModule = builder.getTokenType() == ElmTokenTypes.EFFECT;
        boolean success = Combinators.simpleSequence(builder,
                Combinators.or(
                        expect(ElmTokenTypes.EFFECT, ElmTokenTypes.MODULE),
                        expect(ElmTokenTypes.PORT, ElmTokenTypes.MODULE),
                        expect(ElmTokenTypes.MODULE)),
                Whitespace::maybeWhitespace,
                Basic.dottedCapVar(ElmElementTypes.MODULE_NAME),
                Whitespace::maybeWhitespace,
                isEffectModule ? settings() : empty(),
                Whitespace::maybeWhitespace,
                exposing(),
                Whitespace::freshLine
        );
        if (!success) {
            OnError.consumeUntil(builder, ElmTokenTypes.NEW_LINE);
        }
        marker.done(ElmElementTypes.MODULE_HEADER);
        return true;
    }

    private static Parser settings() {
        return (builder -> {
            sequenceAs(ElmElementTypes.EFFECT_MODULE_SETTINGS,
                    expect(ElmTokenTypes.WHERE),
                    Whitespace::maybeWhitespace,
                    settingsList()
            ).apply(builder);
            return true; //display errors, but try to continue parsing of module header
        });
    }

    private static Parser settingsList() {
        return Combinators.as(ElmElementTypes.EFFECT_MODULE_SETTINGS_LIST,
                Basic.brackets(commaSep(
                        as(ElmElementTypes.EFFECT_MODULE_SETTING,
                                sequence(
                                        expect(ElmTokenTypes.LOW_VAR),
                                        padded(ElmTokenTypes.EQUALS),
                                        expect(ElmTokenTypes.CAP_VAR)
                                ))
                        )
                )
        );
    }

    private static Parser exposing() {
        return Combinators.sequenceAs(ElmElementTypes.EXPOSING_NODE,
                expect(ElmTokenTypes.EXPOSING),
                Whitespace::maybeWhitespace,
                Basic.listing(Module.exportValue()));
    }

    private static NamedParser exportValue() {
        Parser parser = Combinators.orAs(ElmElementTypes.EXPORTED_VALUE,
                expect(ElmTokenTypes.LOW_VAR),
                Basic.operator(),
                Module.typeExport());
        return NamedParser.of("Exported value", parser);
    }

    private static NamedParser typeExport() {
        Parser p = sequence(
                expect(CAP_VAR),
                Basic.listing(expect(CAP_VAR)));
        return NamedParser.of("Type export", p);
    }
}
