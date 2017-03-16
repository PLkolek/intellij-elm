package mkolaczek.elm.features.autocompletion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.completions.KeywordCompletion;
import mkolaczek.elm.features.autocompletion.completions.ModuleCompletion;
import mkolaczek.elm.features.autocompletion.completions.TypeCompletion;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.Arrays.asList;


public class ElmCompletionContributor extends CompletionContributor {
    public ElmCompletionContributor() {
        KeywordCompletion.keywords(this);
        TypeCompletion.types(this);

        ModuleCompletion.modules(this);


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
                             Function<CompletionParameters, Collection<String>> autocompletion) {
        extend(CompletionType.BASIC, pattern, LambdaBasedCompletionProvider.forStrings(autocompletion));
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
