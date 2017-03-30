package mkolaczek.elm.features.autocompletion.completions;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.*;
import mkolaczek.elm.psi.node.extensions.TypeOfExposed;
import mkolaczek.elm.references.Resolver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.features.autocompletion.Patterns.*;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.PsiUtil.getParentOfType2;
import static mkolaczek.elm.psi.Tokens.*;
import static mkolaczek.elm.psi.node.Module.module;

public class TypeCompletion {
    public static void types(ElmCompletionContributor c) {
        //@formatter:off
        c.autocomplete(afterLeaf(e(TYPE)),                                  TypeCompletion::exposedTypes);
        c.autocomplete(afterLeaf(e(ALIAS)),                                 TypeCompletion::exposedConstructorlessTypes);
        c.autocomplete(afterLeaf(EQUALS, PIPE).inside(e(TYPE_DECL_NODE)),   TypeCompletion::exposedTypeConstructors);
        c.autocomplete(e().inside(e(QUALIFIED_TYPE_NAME_REF)),              TypeCompletion::visibleTypes);
        c.autocomplete(e().inside(e(TYPE_NAME_REF)),                        TypeCompletion::visibleTypes);
        c.autocomplete(inExposing(TYPE_NAME_REF),                           TypeCompletion::notExposedTypes);
        //@formatter:on
    }

    private static Stream<String> visibleTypes(CompletionParameters parameters) {
        Optional<TypeAliasDeclNode> aliasDeclNode = getParentOfType2(parameters.getPosition(), TypeAliasDeclNode.class);
        String aliasName = aliasDeclNode.map(a -> a.typeDeclaration().getName()).orElse(null);
        return Resolver.forTypes()
                       .variants(parameters.getPosition())
                       .filter(s -> !s.equals(aliasName));
    }

    private static Stream<String> exposedConstructorlessTypes(CompletionParameters parameters) {
        return module(parameters.getPosition())
                .exposed(TypeOfExposed.TYPE)
                .map(t -> (TypeExposing) t)
                .filter(TypeExposing::withoutConstructors)
                .map(TypeExposing::exposedName);
    }

    private static Stream<String> exposedTypes(CompletionParameters parameters) {
        return module(parameters.getPosition())
                .exposed(TypeOfExposed.TYPE)
                .map(t -> (TypeExposing) t)
                .flatMap(TypeCompletion::typeCompletions);
    }

    private static Stream<String> typeCompletions(TypeExposing typeExposing) {
        ArrayList<String> result = Lists.newArrayList(typeExposing.exposedName());
        if (!typeExposing.withoutConstructors()) {
            result.add(TypeExposing.declarationString(typeExposing));
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
        Collection<String> constructors = header.flatMap(h -> h.exposedType(typeName))
                                                .map(t -> (TypeExposing) t)
                                                .map(TypeExposing::constructorNames)
                                                .orElse(newArrayList());
        Set<String> declared = declaration.constructors()
                                          .map(TypeConstructor::getText)
                                          .collect(toSet());

        constructors.removeAll(declared);
        return constructors;
    }

    private static Stream<String> notExposedTypes(CompletionParameters parameters) {
        return ElmCompletionContributor.notExposed(TypeOfExposed.TYPE, parameters);
    }
}
