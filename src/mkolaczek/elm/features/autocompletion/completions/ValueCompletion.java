package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.OperatorDeclaration;
import mkolaczek.util.Streams;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.features.autocompletion.Patterns.*;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.DIGIT;
import static mkolaczek.elm.psi.node.Module.module;

public class ValueCompletion {
    public static void values(ElmCompletionContributor c) {
        c.autocompletePlain(
                afterLeaf(e(DIGIT).inside(e(INFIX_OPERATOR_DECLARATION))),
                ValueCompletion::exposedOperators
        );
        c.autocomplete(
                e().andOr(e().inside(e(OPERATOR)), childOf(RUNE_OF_AUTOCOMPLETION_EL)).inside(e(MODULE_HEADER)),
                ValueCompletion::moduleOperators
        );

    }

    private static Stream<String> moduleOperators(CompletionParameters parameters) {
        Set<String> excluded = exposedOperators(parameters).map(OperatorDeclaration::parens).collect(toSet());
        return module(parameters.getPosition())
                .operatorDeclarations()
                .map(OperatorDeclaration::parensName)
                .flatMap(Streams::stream)
                .filter(o -> !excluded.contains(o));
    }

    private static Stream<String> exposedOperators(CompletionParameters parameters) {
        return module(parameters.getPosition())
                .operatorSymbolExports()
                .map(PsiElement::getText);
    }

}
