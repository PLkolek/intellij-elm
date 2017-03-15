package mkolaczek.elm.features.autocompletion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

class LambdaBasedCompletionProvider extends CompletionProvider<CompletionParameters> {
    private final Function<CompletionParameters, Collection<LookupElementBuilder>> autocompletion;

    public LambdaBasedCompletionProvider(Function<CompletionParameters, Collection<LookupElementBuilder>> autocompletion) {
        this.autocompletion = autocompletion;
    }

    public static LambdaBasedCompletionProvider forStrings(Function<CompletionParameters, Collection<String>> autocompletion) {
        Function<Collection<String>, Collection<LookupElementBuilder>> wrapper = strings -> strings.stream()
                                                                                                   .map(LookupElementBuilder::create)
                                                                                                   .collect(toList());
        return new LambdaBasedCompletionProvider(wrapper.compose(autocompletion));
    }


    @Override
    public void addCompletions(@NotNull CompletionParameters parameters,
                               ProcessingContext context,
                               @NotNull CompletionResultSet resultSet) {
        resultSet.addAllElements(autocompletion.apply(parameters));
    }
}
