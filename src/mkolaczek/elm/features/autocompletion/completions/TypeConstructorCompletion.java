package mkolaczek.elm.features.autocompletion.completions;

import com.google.common.collect.Sets;
import com.intellij.codeInsight.completion.CompletionParameters;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.*;
import mkolaczek.elm.psi.node.extensions.QualifiedRef;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;

import java.util.Set;
import java.util.stream.Stream;

import static mkolaczek.elm.features.autocompletion.ElmCompletionContributor.location;
import static mkolaczek.elm.features.autocompletion.Patterns.childOf;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.RUNE_OF_AUTOCOMPLETION;
import static mkolaczek.elm.psi.node.Module.module;

public class TypeConstructorCompletion {

    public static void typeConstructors(ElmCompletionContributor c) {
        c.autocomplete(
                childOf(TYPE_CONSTRUCTOR_REF).inside(e(MODULE_HEADER)), TypeConstructorCompletion::constructorsFromType
        );
        c.autocomplete(e().inside(e(QUALIFIED_TYPE_CONSTRUCTOR_REF)),
                TypeConstructorCompletion::constructorsFromModule);
        c.autocomplete(e().inside(e(QUALIFIED_VAR)),
                TypeConstructorCompletion::constructorsFromModule);
        c.autocomplete(e(RUNE_OF_AUTOCOMPLETION).inside(e(PATTERN_TERM)),
                TypeConstructorCompletion::nonQualifiedConstructors
        );

        //@formatter:on
    }

    private static Stream<String> constructorsFromModule(CompletionParameters parameters) {
        Module module = module(parameters.getPosition());
        QualifiedRef qualifiedConstructor = location(parameters, QualifiedRef.class);
        if (qualifiedConstructor != null && qualifiedConstructor.moduleName().isPresent()) {
            return module.imports(qualifiedConstructor.moduleName().get().getName())
                         .flatMap(Import::importedModule)
                         .flatMap(Module::exportedConstructors)
                         .map(TypeConstructor::getName);
        }

        return nonQualifiedConstructors(parameters);
    }

    private static Stream<String> nonQualifiedConstructors(CompletionParameters parameters) {
        Module module = module(parameters.getPosition());

        Stream<TypeConstructor> exposedConstructors = module.imports().flatMap(i ->
                i.importedModule()
                 .flatMap(m -> i.filterExposedConstructors(m.exportedConstructorsMap()).values().stream())
        );

        return Stream.concat(module.constructorDeclarations(), exposedConstructors).map(TypeConstructor::getName);
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

