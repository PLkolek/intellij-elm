package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.Operator;
import mkolaczek.util.Streams;

import java.util.stream.Stream;

import static mkolaczek.elm.features.autocompletion.Patterns.afterLeaf;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.INFIX_OPERATOR_DECLARATION;
import static mkolaczek.elm.psi.Tokens.DIGIT;
import static mkolaczek.elm.psi.node.Module.module;

public class ValueCompletion {
    public static void values(ElmCompletionContributor c) {
        c.autocompletePlain(
                afterLeaf(e(DIGIT).inside(e(INFIX_OPERATOR_DECLARATION))),
                ValueCompletion::exposedOperators
        );
    }

    private static Stream<String> exposedOperators(CompletionParameters parameters) {
        return module(parameters.getPosition()).operatorExports()
                                               .map(Operator::symbol)
                                               .flatMap(Streams::stream)
                                               .map(PsiElement::getText);
    }

}
