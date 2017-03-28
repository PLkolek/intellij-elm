package mkolaczek.elm.features.autocompletion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.completions.*;
import mkolaczek.elm.features.autocompletion.providers.LambdaBasedCompletionProvider;
import mkolaczek.elm.features.autocompletion.providers.PlainMatchingCompletionProvider;
import mkolaczek.elm.psi.node.extensions.Exposed;
import mkolaczek.elm.psi.node.extensions.HasExposing;
import mkolaczek.elm.psi.node.extensions.TypeOfExposed;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;


public class ElmCompletionContributor extends CompletionContributor {

    public static final String AUTOCOMPLETION_RUNE_SYMBOL = "\u16DC";

    public ElmCompletionContributor() {
        KeywordCompletion.keywords(this);
        TypeCompletion.types(this);
        ModuleCompletion.modules(this);
        ValueCompletion.values(this);
        TypeConstructorCompletion.typeConstructors(this);
    }

    public static <T extends PsiElement> T location(CompletionParameters parameters, Class<T> parentType) {
        return location(parameters.getPosition(), parentType);
    }

    public static <T extends PsiElement> T location(PsiElement position, Class<T> parentType) {
        T parent = getParentOfType(position, parentType);
        assert parent != null;
        return parent;
    }

    public static Stream<String> notExposed(TypeOfExposed<? extends Exposed> typeOfExposed,
                                            CompletionParameters parameters) {
        HasExposing hasExposing = getParentOfType(parameters.getPosition(), HasExposing.class);
        assert hasExposing != null;
        Set<String> exposed = hasExposing.exposed(typeOfExposed).map(Exposed::exposedName).collect(toSet());
        return typeOfExposed.resolver().variants(parameters.getPosition())
                            .filter(o -> !exposed.contains(o));
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
