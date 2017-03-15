package mkolaczek.elm.features.autocompletion;

import com.google.common.base.Preconditions;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.ModuleNameRef;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.stream.Collectors.toList;
import static mkolaczek.elm.features.autocompletion.Patterns.afterLeaf;


public class ElmCompletionContributor extends CompletionContributor {
    public ElmCompletionContributor() {
        KeywordCompletion.keywords(this);

        autocomplete(afterLeaf(Tokens.MODULE), ElmCompletionContributor::fileName);
        autocomplete(afterLeaf(Tokens.AS), ElmCompletionContributor::moduleNameParts);
        TypesCompletion.types(this);


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
