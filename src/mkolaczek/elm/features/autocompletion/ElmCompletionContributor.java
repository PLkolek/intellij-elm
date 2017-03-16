package mkolaczek.elm.features.autocompletion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.completions.KeywordCompletion;
import mkolaczek.elm.features.autocompletion.completions.ModuleCompletion;
import mkolaczek.elm.features.autocompletion.completions.TypeCompletion;
import mkolaczek.elm.features.autocompletion.completions.ValueCompletion;
import mkolaczek.elm.features.autocompletion.providers.LambdaBasedCompletionProvider;
import mkolaczek.elm.features.autocompletion.providers.PlainMatchingCompletionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.Arrays.asList;


public class ElmCompletionContributor extends CompletionContributor {

    public static final String AUTOCOMPLETION_RUNE_SYMBOL = "\u16DC";

    public ElmCompletionContributor() {
        KeywordCompletion.keywords(this);
        TypeCompletion.types(this);
        ModuleCompletion.modules(this);
        ValueCompletion.values(this);


    }

    public static <T extends PsiElement> T location(CompletionParameters parameters, Class<T> parentType) {
        return location(parameters.getPosition(), parentType);
    }

    public static <T extends PsiElement> T location(PsiElement position, Class<T> parentType) {
        T parent = getParentOfType(position, parentType);
        assert parent != null;
        return parent;
    }

    public void autocomplete(Capture<PsiElement> pattern, LookupElementBuilder... completions) {
        extend(CompletionType.BASIC, pattern, new LambdaBasedCompletionProvider(parameters -> asList(completions)));
    }


    public void autocomplete(Capture<PsiElement> pattern,
                             Function<CompletionParameters, Stream<String>> autocompletion) {
        extend(CompletionType.BASIC, pattern, LambdaBasedCompletionProvider.forStrings(autocompletion));
    }

    public void autocompletePlain(Capture<PsiElement> pattern,
                                  Function<CompletionParameters, Stream<String>> autocompletion) {
        extend(CompletionType.BASIC, pattern,
                new PlainMatchingCompletionProvider(
                        LambdaBasedCompletionProvider.forStrings(autocompletion)
                )
        );
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        super.fillCompletionVariants(parameters, result);
    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        context.setDummyIdentifier(AUTOCOMPLETION_RUNE_SYMBOL);
    }
}
