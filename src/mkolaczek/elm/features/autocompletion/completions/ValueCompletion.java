package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.features.autocompletion.Patterns.afterLeaf;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.DIGIT;
import static mkolaczek.elm.psi.node.Module.module;

public class ValueCompletion {
    public static void values(ElmCompletionContributor c) {
        c.autocompletePlain(
                afterLeaf(e(DIGIT).inside(e(INFIX_OPERATOR_DECLARATION))),
                ValueCompletion::exposedOperators
        );
        c.autocompletePlain(
                e().inside(e(OPERATOR_SYMBOL)).inside(e(MODULE_HEADER)),
                ValueCompletion::moduleOperators
        );

    }

    private static Stream<String> moduleOperators(CompletionParameters parameters) {
        Set<String> excluded = exposedOperators(parameters).collect(toSet());
        return module(parameters.getPosition())
                .operatorDeclarations()
                .map(PsiNamedElement::getName)
                .filter(o -> !excluded.contains(o));
    }

    private static Stream<String> exposedOperators(CompletionParameters parameters) {
        return module(parameters.getPosition())
                .operatorSymbolExports()
                .map(PsiElement::getText);
    }

}
