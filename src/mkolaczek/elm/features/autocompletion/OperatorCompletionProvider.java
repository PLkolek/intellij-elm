package mkolaczek.elm.features.autocompletion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PlainPrefixMatcher;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import mkolaczek.elm.psi.node.Operator;
import mkolaczek.util.Streams;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static mkolaczek.elm.psi.node.Module.module;

public class OperatorCompletionProvider extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        String symbolText = parameters.getPosition().getText();
        String prefix = symbolText.substring(0,
                symbolText.indexOf(ElmCompletionContributor.AUTOCOMPLETION_RUNE_SYMBOL));
        result = result.withPrefixMatcher(new PlainPrefixMatcher(prefix));
        List<LookupElementBuilder> symbols = module(parameters.getPosition()).operatorExports()
                                                                             .map(Operator::symbol)
                                                                             .flatMap(Streams::stream)
                                                                             .map(PsiElement::getText)
                                                                             .map(LookupElementBuilder::create)
                                                                             .collect(toList());
        result.addAllElements(symbols);
    }
}
