package mkolaczek.elm.features.autocompletion.completions;

import com.google.common.collect.Sets;
import com.intellij.codeInsight.completion.CompletionParameters;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.TypeExposing;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import mkolaczek.elm.references.Resolver;

import java.util.Set;
import java.util.stream.Stream;

import static mkolaczek.elm.features.autocompletion.ElmCompletionContributor.location;
import static mkolaczek.elm.features.autocompletion.Patterns.childOf;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.RUNE_OF_AUTOCOMPLETION;
import static mkolaczek.elm.psi.node.Module.module;

public class TypeConstructorCompletion {

    private static final Resolver<?> resolver = Resolver.forTypeConstructors();

    public static void typeConstructors(ElmCompletionContributor c) {
        c.autocomplete(
                childOf(TYPE_CONSTRUCTOR_REF).inside(e(MODULE_HEADER)), TypeConstructorCompletion::constructorsFromType
        );
        c.autocomplete(e().inside(e(QUALIFIED_TYPE_CONSTRUCTOR_REF)),
                TypeConstructorCompletion::visibleConstructors);
        c.autocomplete(e().inside(e(QUALIFIED_VAR)),
                TypeConstructorCompletion::visibleConstructors);
        c.autocomplete(e(RUNE_OF_AUTOCOMPLETION).inside(e(PATTERN_TERM)),
                TypeConstructorCompletion::nonQualifiedConstructors
        );

        //@formatter:on
    }

    private static Stream<String> visibleConstructors(CompletionParameters parameters) {
        return resolver.variants(parameters.getPosition());
    }

    private static Stream<String> nonQualifiedConstructors(CompletionParameters parameters) {
        return resolver.unqualifiedVariants(parameters.getPosition());
    }

    private static Stream<String> constructorsFromType(CompletionParameters parameters) {
        TypeExposing typeExposing = location(parameters, TypeExposing.class);

        Set<String> excluded = Sets.newHashSet(typeExposing.constructorNames());

        return module(typeExposing).declarations(TypeOfDeclaration.TYPE, typeExposing.typeNameString())
                                   .flatMap(TypeDeclaration::constructors)
                                   .filter(elem -> !excluded.contains(elem.getName()))
                                   .map(TypeConstructor::getName);
    }
}

