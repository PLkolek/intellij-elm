package mkolaczek.elm.features.autocompletion.completions;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.ModuleHeader;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.TypeExport;
import mkolaczek.elm.references.TypeReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.features.autocompletion.Patterns.afterLeaf;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.QUALIFIED_TYPE_NAME_REF;
import static mkolaczek.elm.psi.Elements.TYPE_DECL_NODE;
import static mkolaczek.elm.psi.Tokens.*;
import static mkolaczek.elm.psi.node.Module.module;

public class TypeCompletion {
    public static void types(ElmCompletionContributor c) {
        //@formatter:off
        c.autocomplete(afterLeaf(e(TYPE)),                                  TypeCompletion::exposedTypes);
        c.autocomplete(afterLeaf(e(ALIAS)),                                 TypeCompletion::exposedConstructorlessTypes);
        c.autocomplete(afterLeaf(EQUALS, PIPE).inside(e(TYPE_DECL_NODE)),   TypeCompletion::exposedTypeConstructors);
        c.autocomplete(e().inside(e(QUALIFIED_TYPE_NAME_REF)),              TypeCompletion::typesFromModule);
        //@formatter:on
    }

    private static Stream<String> typesFromModule(CompletionParameters parameters) {
        return stream(TypeReference.variants(parameters.getPosition())).map(TypeDeclaration::getName);
    }

    private static Stream<String> exposedConstructorlessTypes(CompletionParameters parameters) {
        return module(parameters.getPosition())
                .typeExports()
                .filter(TypeExport::withoutConstructors)
                .map(TypeExport::typeNameString);
    }

    private static Stream<String> exposedTypes(CompletionParameters parameters) {
        return module(parameters.getPosition())
                .typeExports()
                .flatMap(TypeCompletion::typeCompletions);
    }

    private static Stream<String> typeCompletions(TypeExport typeExport) {
        ArrayList<String> result = Lists.newArrayList(typeExport.typeNameString());
        if (!typeExport.withoutConstructors()) {
            result.add(TypeExport.declarationString(typeExport));
        }
        return result.stream();
    }

    private static Stream<String> exposedTypeConstructors(CompletionParameters parameters) {
        Collection<String> constructors = undeclaredConstructors(parameters.getPosition());
        if (constructors.size() > 1) {
            constructors.add(Joiner.on(" | ").join(constructors));
        }
        return constructors.stream();
    }

    @NotNull
    private static Collection<String> undeclaredConstructors(PsiElement position) {
        TypeDeclaration declaration = ElmCompletionContributor.location(position, TypeDeclaration.class);
        String typeName = declaration.getName();

        Optional<ModuleHeader> header = module(position).header();
        Collection<String> constructors = header.flatMap(h -> h.typeExport(typeName))
                                                .map(TypeExport::constructorNames)
                                                .orElse(newArrayList());
        Set<String> declared = declaration.constructors()
                                          .map(TypeConstructor::getText)
                                          .collect(toSet());

        constructors.removeAll(declared);
        return constructors;
    }
}
