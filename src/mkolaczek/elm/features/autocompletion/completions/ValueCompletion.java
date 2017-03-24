package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.OperatorDeclaration;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.elm.psi.node.extensions.TypeOfExposed;
import mkolaczek.util.Streams;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.features.autocompletion.Patterns.*;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;
import static mkolaczek.elm.psi.node.Module.module;

public class ValueCompletion {
    public static void values(ElmCompletionContributor c) {
        c.autocompletePlain(
                afterLeaf(e(DIGIT)).inside(e(INFIX_OPERATOR_DECLARATION)),
                parameters -> exposed(parameters, TypeOfExposed.OPERATOR)
        );
        c.autocomplete(afterLeaf(e(PORT).inside(e(PORT_DECLARATION))), params -> exposed(params, TypeOfExposed.VALUE));
        c.autocomplete(e().atStartOf(e(VALUE_DECLARATION)), params -> exposed(params, TypeOfExposed.VALUE));
        c.autocomplete(e().atStartOf(e(VALUE_DECLARATION)),
                params -> exposed(params, TypeOfExposed.OPERATOR).map(OperatorDeclaration::parens));
        c.autocomplete(e().inside((e(OPERATOR).atStartOf(e(VALUE_DECLARATION)))),
                params -> exposed(params, TypeOfExposed.OPERATOR).map(OperatorDeclaration::parens));
        //noinspection unchecked
        c.autocomplete(e().andOr(e(RUNE_OF_AUTOCOMPLETION), e().inside(e(VALUE_NAME)))
                          .inside(e(PATTERN_TERM).atStartOf(e(VALUE_DECLARATION))),
                params -> exposed(params, TypeOfExposed.VALUE)
        );

        c.autocomplete(
                e().andOr(e().inside(e(OPERATOR)), childOf(RUNE_OF_AUTOCOMPLETION_EL)).inside(e(MODULE_HEADER)),
                ValueCompletion::moduleOperators
        );

        c.autocomplete(
                e().andOr(e().inside(e(VALUE_EXPOSING)), childOf(RUNE_OF_AUTOCOMPLETION_EL)).inside(e(MODULE_HEADER)),
                ValueCompletion::moduleValues
        );

    }

    private static Stream<String> moduleOperators(CompletionParameters parameters) {
        return declared(parameters,
                TypeOfExposed.OPERATOR,
                Declaration::declaredOperatorName).map(OperatorDeclaration::parens);
    }

    private static Stream<String> moduleValues(CompletionParameters parameters) {
        return declared(parameters, TypeOfExposed.VALUE, Declaration::declaredValueNames);
    }

    private static Stream<String> declared(CompletionParameters parameters,
                                           TypeOfExposed<?> typeOfExposed,
                                           Function<Declaration, Stream<String>> valueExtractor) {
        Set<String> excluded = exposed(parameters, typeOfExposed).collect(toSet());
        return module(parameters.getPosition()).declarations()
                                               .flatMap(valueExtractor)
                                               .flatMap(Streams::stream)
                                               .filter(o -> !excluded.contains(o));
    }

    private static Stream<String> exposed(CompletionParameters parameters,
                                          TypeOfExposed<? extends PsiElement> exposedElementsType) {
        return module(parameters.getPosition())
                .exposed(exposedElementsType)
                .map(PsiElement::getText);
    }
}
