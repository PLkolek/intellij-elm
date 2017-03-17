package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.OperatorDeclaration;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import mkolaczek.elm.psi.node.extensions.TypeOfExport;
import mkolaczek.util.Streams;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.features.autocompletion.Patterns.*;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.DIGIT;
import static mkolaczek.elm.psi.Tokens.PORT;
import static mkolaczek.elm.psi.node.Module.module;

public class ValueCompletion {
    public static void values(ElmCompletionContributor c) {
        c.autocompletePlain(
                afterLeaf(e(DIGIT).inside(e(INFIX_OPERATOR_DECLARATION))),
                parameters -> exposed(parameters, TypeOfExport.OPERATOR)
        );
        c.autocomplete(
                e().andOr(e().inside(e(OPERATOR)), childOf(RUNE_OF_AUTOCOMPLETION_EL)).inside(e(MODULE_HEADER)),
                ValueCompletion::moduleOperators
        );

        c.autocomplete(
                e().andOr(e().inside(e(VALUE_EXPORT)), childOf(RUNE_OF_AUTOCOMPLETION_EL)).inside(e(MODULE_HEADER)),
                ValueCompletion::moduleValues
        );

        c.autocomplete(afterLeaf(e(PORT).inside(e(PORT_DECLARATION))), params -> exposed(params, TypeOfExport.VALUE));

    }

    private static Stream<String> moduleOperators(CompletionParameters parameters) {
        return declared(parameters, TypeOfDeclaration.OPERATOR, OperatorDeclaration::parens);
    }

    private static Stream<String> moduleValues(CompletionParameters parameters) {
        return declared(parameters, TypeOfDeclaration.PORT, identity());
    }

    private static Stream<String> declared(CompletionParameters parameters,
                                           TypeOfDeclaration<?, ?> typeOfDeclaration,
                                           Function<String, String> namePresenter) {
        Set<String> excluded = exposed(parameters, typeOfDeclaration.exportedAs())
                .map(namePresenter)
                .collect(toSet());
        return module(parameters.getPosition()).declarations(typeOfDeclaration)
                                               .map(PsiNamedElement::getName)
                                               .flatMap(Streams::stream)
                                               .map(namePresenter)
                                               .filter(o -> !excluded.contains(o));
    }

    private static Stream<String> exposed(CompletionParameters parameters,
                                          TypeOfExport<? extends PsiElement> exposedElementsType) {
        return module(parameters.getPosition())
                .exports(exposedElementsType)
                .map(PsiElement::getText);
    }
}
