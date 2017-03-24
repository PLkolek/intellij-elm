package mkolaczek.elm.features.autocompletion.completions;

import com.google.common.collect.Sets;
import com.intellij.codeInsight.completion.CompletionParameters;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.*;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;

import java.util.Set;
import java.util.stream.Stream;

import static mkolaczek.elm.features.autocompletion.ElmCompletionContributor.location;
import static mkolaczek.elm.features.autocompletion.Patterns.childOf;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.node.Module.module;

public class TypeConstructorCompletion {

    public static void typeConstructors(ElmCompletionContributor c) {
        c.autocomplete(
                childOf(TYPE_CONSTRUCTOR_REF).inside(e(MODULE_HEADER)), TypeConstructorCompletion::constructorsFromType
        );
        c.autocomplete(e().inside(e(QUALIFIED_TYPE_CONSTRUCTOR_REF)),
                TypeConstructorCompletion::constructorsFromModule);

        //@formatter:on
    }

    private static Stream<String> constructorsFromModule(CompletionParameters parameters) {
        QualifiedTypeConstructorRef qualifiedConstructor = location(parameters, QualifiedTypeConstructorRef.class);
        if (qualifiedConstructor != null && qualifiedConstructor.moduleName().isPresent()) {
            return module(qualifiedConstructor).imports(qualifiedConstructor.moduleName().get().getName())
                                               .flatMap(Import::importedModule)
                                               .flatMap(Module::exportedConstructors)
                                               .map(TypeConstructor::getName);
        }
        return Stream.empty();
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

