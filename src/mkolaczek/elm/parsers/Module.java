package mkolaczek.elm.parsers;

import com.google.common.collect.ImmutableSet;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.faultTolerant.ModuleDeclaration;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.Combinators.*;
import static mkolaczek.elm.psi.Tokens.CAP_VAR;

public class Module {
    public static void module(@NotNull PsiBuilder builder) {
        Whitespace.freshLine(builder);
        simpleTry(builder, Module::moduleDeclaration);
        new ModuleDeclaration().parse(builder);
        simpleTry(builder, sequence(Comment.docComment(), Whitespace::freshLine));
        simpleManyAs(builder, Elements.IMPORTS, Module::importLine);
    }

    private static boolean importLine(PsiBuilder builder) {
        if (builder.getTokenType() != Tokens.IMPORT) {
            return false;
        }
        PsiBuilder.Marker m = builder.mark();
        boolean result = Combinators.simpleSequence(builder,
                expect(Tokens.IMPORT),
                Whitespace::maybeWhitespace,
                Basic.dottedCapVar(Elements.MODULE_NAME_REF)
        );
        if (!result) {
            OnError.consumeUntil(builder, Tokens.NEW_LINE);
            m.done(Elements.IMPORT_LINE);
            return false;
        }
        PsiBuilder.Marker m2 = builder.mark();
        boolean hasAs = Combinators.simpleSequence(builder,
                Whitespace::maybeWhitespace,
                expect(Tokens.AS));
        if (hasAs) {
            m2.drop();
            if (!Combinators.simpleSequence(builder,
                    Whitespace::maybeWhitespace,
                    Combinators.expectAs(Elements.MODULE_ALIAS, CAP_VAR))) {
                OnError.consumeUntil(builder, Tokens.NEW_LINE);
                m.done(Elements.IMPORT_LINE);
                return false;
            }
        } else {
            m2.rollbackTo();
        }
        m2 = builder.mark();
        boolean hasExposing = Combinators.simpleSequence(builder,
                Whitespace::maybeWhitespace,
                expect(Tokens.EXPOSING));
        m2.rollbackTo();
        if (hasExposing) {
            Whitespace.maybeWhitespace(builder);

            if (!exposing().apply(builder)) {
                OnError.consumeUntil(builder, Tokens.NEW_LINE);
                m.done(Elements.IMPORT_LINE);
                return false;
            }
        }

        boolean success = Whitespace.freshLine(builder);
        m.done(Elements.IMPORT_LINE);
        return success;
    }

    private static boolean moduleDeclaration(@NotNull PsiBuilder builder) {
        //noinspection SuspiciousMethodCalls
        if (!ImmutableSet.of(Tokens.MODULE, Tokens.PORT, Tokens.EFFECT).contains(builder.getTokenType())) {
            return false;
        }
        PsiBuilder.Marker marker = builder.mark();
        boolean isEffectModule = builder.getTokenType() == Tokens.EFFECT;
        Combinators.simpleSequence(builder,
                Combinators.or(
                        expect(Tokens.EFFECT, Tokens.MODULE),
                        expect(Tokens.PORT, Tokens.MODULE),
                        expect(Tokens.MODULE)),
                Whitespace::maybeWhitespace,
                Combinators.skipUntilFL(Basic.dottedCapVar(Elements.MODULE_NAME)),
                Whitespace::maybeWhitespace,
                isEffectModule ? Combinators.skipUntilFL(settings(), Tokens.EXPOSING) : empty(),
                Whitespace::maybeWhitespace,
                exposing(),
                Whitespace::freshLine
        );
        marker.done(Elements.MODULE_HEADER);
        return true;
    }

    private static NamedParser settings() {
        return NamedParser.of("Effect module settings",
                builder -> {
                    if (builder.getTokenType() != Tokens.WHERE) {
                        return false;
                    }
                    sequenceAs(Elements.EFFECT_MODULE_SETTINGS,
                            expect(Tokens.WHERE),
                            Whitespace::maybeWhitespace,
                            settingsList()
                    ).apply(builder);
                    return true;
                });
    }

    private static Parser settingsList() {
        return Combinators.as(Elements.EFFECT_MODULE_SETTINGS_LIST,
                Basic.brackets(commaSep(
                        as(Elements.EFFECT_MODULE_SETTING,
                                sequence(
                                        expect(Tokens.LOW_VAR),
                                        padded(Tokens.EQUALS),
                                        expect(Tokens.CAP_VAR)
                                ))
                        )
                )
        );
    }

    private static Parser exposing() {
        return Combinators.sequenceAs(Elements.EXPOSING_NODE,
                expect(Tokens.EXPOSING),
                Whitespace::maybeWhitespace,
                Basic.listing(Module.exportValue()));
    }

    private static NamedParser exportValue() {
        Parser parser = Combinators.orAs(Elements.EXPORTED_VALUE,
                expect(Tokens.LOW_VAR),
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
