package mkolaczek.elm.features.autocompletion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PlainPrefixMatcher;
import com.intellij.util.ProcessingContext;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import org.jetbrains.annotations.NotNull;

public class PlainMatchingCompletionProvider extends CompletionProvider<CompletionParameters> {

    private final CompletionProvider<CompletionParameters> completionProvider;

    public PlainMatchingCompletionProvider(CompletionProvider<CompletionParameters> completionProvider) {
        this.completionProvider = completionProvider;
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        String symbolText = parameters.getPosition().getText();
        String prefix = symbolText.substring(0,
                symbolText.indexOf(ElmCompletionContributor.AUTOCOMPLETION_RUNE_SYMBOL));
        result = result.withPrefixMatcher(new PlainPrefixMatcher(prefix));
        completionProvider.addCompletionVariants(parameters, context, result);
    }
}
