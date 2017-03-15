package mkolaczek.elm.features.autocompletion;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.features.autocompletion.Patterns.afterLeaf;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.TYPE_DECL_NODE;
import static mkolaczek.elm.psi.Tokens.*;
import static mkolaczek.elm.psi.node.Module.module;


public class ElmCompletionContributor extends CompletionContributor {
    public ElmCompletionContributor() {
        KeywordCompletion.keywords(this);

        autocomplete(afterLeaf(Tokens.MODULE), ElmCompletionContributor::fileName);
        autocomplete(afterLeaf(Tokens.AS), ElmCompletionContributor::moduleNameParts);
        autocomplete(afterLeaf(e(TYPE)), ElmCompletionContributor::exposedTypes);
        autocomplete(afterLeaf(e(ALIAS)), ElmCompletionContributor::exposedConstructorlessTypes);


        autocomplete(afterLeaf(EQUALS, PIPE).inside(e(TYPE_DECL_NODE)),
                ElmCompletionContributor::exposedTypeConstructors);


    }

    private static Collection<String> exposedTypeConstructors(CompletionParameters parameters) {
        TypeDeclaration declaration = getParentOfType(parameters.getPosition(), TypeDeclaration.class);
        assert declaration != null;
        String typeName = declaration.getName();

        Optional<ModuleHeader> header = module(parameters.getPosition()).header();
        Collection<String> constructors = header.flatMap(h -> h.typeExport(typeName))
                                                .map(TypeExport::constructorNames)
                                                .orElse(newArrayList());
        Set<String> presentConstructors = declaration.constructors()
                                                     .map(TypeConstructor::getText)
                                                     .collect(toSet());

        constructors.removeAll(presentConstructors);
        if (constructors.size() > 1) {
            constructors.add(Joiner.on(" | ").join(constructors));
        }
        return constructors;
    }

    private static Collection<String> exposedConstructorlessTypes(CompletionParameters parameters) {
        return module(parameters.getPosition())
                .typeExports()
                .filter(TypeExport::withoutConstructors)
                .map(TypeExport::typeNameString)
                .collect(toList());
    }

    private static Collection<String> exposedTypes(CompletionParameters parameters) {
        return module(parameters.getPosition())
                .typeExports()
                .flatMap(ElmCompletionContributor::typeCompletions)
                .collect(toList());
    }

    @NotNull
    private static Collection<String> moduleNameParts(CompletionParameters parameters) {
        Import importLine = getParentOfType(parameters.getPosition(), Import.class);
        Preconditions.checkState(importLine != null, "As must be a child of import line");
        ModuleNameRef module = importLine.importedModuleName();
        String[] words = module.getName().split("\\.");
        return Names.suggest(words);
    }

    @NotNull
    private static Collection<String> fileName(CompletionParameters parameters) {
        String fileName = parameters.getOriginalFile().getName();
        String moduleName = fileName.substring(0, fileName.length() - 4);//cut out .elm
        return newArrayList(moduleName);
    }

    private static Stream<String> typeCompletions(TypeExport typeExport) {
        ArrayList<String> result = Lists.newArrayList(typeExport.typeNameString());
        if (!typeExport.withoutConstructors()) {
            result.add(typeDeclaration(typeExport));
        }
        return result.stream();
    }

    private static String typeDeclaration(TypeExport typeExport) {
        return typeExport.typeNameString() + " = " + Joiner.on(" | ").join(typeExport.constructorNames());
    }

    void autocomplete(Capture<PsiElement> pattern, LookupElementBuilder... completions) {
        autocomplete2(pattern, parameters -> Arrays.asList(completions));
    }

    void autocomplete(Capture<PsiElement> pattern,
                      Function<CompletionParameters, Collection<String>> autocompletion) {
        Function<Collection<String>, Collection<LookupElementBuilder>> wrapper = strings -> strings.stream()
                                                                                                   .map(LookupElementBuilder::create)
                                                                                                   .collect(toList());
        autocomplete2(pattern, wrapper.compose(autocompletion));
    }


    private void autocomplete2(Capture<PsiElement> pattern,
                               Function<CompletionParameters, Collection<LookupElementBuilder>> autocompletion) {
        extend(CompletionType.BASIC, pattern,
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        resultSet.addAllElements(autocompletion.apply(parameters));
                    }
                }
        );
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        super.fillCompletionVariants(parameters, result);
    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        context.setDummyIdentifier("\u16DC");
    }

}
