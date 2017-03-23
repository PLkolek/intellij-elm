package mkolaczek.elm.features.autocompletion.completions;

import com.google.common.collect.Sets;
import com.intellij.codeInsight.completion.CompletionParameters;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.TypeExport;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;

import java.util.Set;
import java.util.stream.Stream;

import static mkolaczek.elm.features.autocompletion.Patterns.childOf;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.MODULE_HEADER;
import static mkolaczek.elm.psi.Elements.TYPE_CONSTRUCTOR_REF;
import static mkolaczek.elm.psi.node.Module.module;

public class TypeConstructorCompletion {

    public static void typeConstructors(ElmCompletionContributor c) {
        //@formatter:off
        c.autocomplete(
                childOf(TYPE_CONSTRUCTOR_REF).inside(e(MODULE_HEADER)),
                TypeConstructorCompletion::constructorsFromType
        );

        //@formatter:on
    }

    private static Stream<String> constructorsFromType(CompletionParameters parameters) {
        TypeExport typeExport = ElmCompletionContributor.location(parameters, TypeExport.class);

        Set<String> excluded = Sets.newHashSet(typeExport.constructorNames());

        return module(typeExport).declarations(TypeOfDeclaration.TYPE, typeExport.typeNameString())
                                 .flatMap(TypeDeclaration::constructors)
                                 .filter(elem -> !excluded.contains(elem.getName()))
                                 .map(TypeConstructor::getName);
    }
}

