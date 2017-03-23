package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.node.OperatorDeclaration;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.elm.psi.node.extensions.TypeOfExport;
import mkolaczek.util.Streams;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.features.autocompletion.Patterns.*;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.DIGIT;
import static mkolaczek.elm.psi.Tokens.PORT;
import static mkolaczek.elm.psi.node.Module.module;

public class ValueCompletion {
    public static void values(ElmCompletionContributor c) {
        c.autocompletePlain(
                afterLeaf(e(DIGIT)).inside(e(INFIX_OPERATOR_DECLARATION)),
                parameters -> exposed(parameters, TypeOfExport.OPERATOR)
        );
        c.autocomplete(afterLeaf(e(PORT).inside(e(PORT_DECLARATION))), params -> exposed(params, TypeOfExport.VALUE));
        c.autocomplete(e().atStartOf(e(VALUE_DECLARATION)), params -> exposed(params, TypeOfExport.VALUE));
        c.autocomplete(e().atStartOf(e(VALUE_DECLARATION)),
                params -> exposed(params, TypeOfExport.OPERATOR).map(OperatorDeclaration::parens));
        c.autocomplete(e().inside((e(OPERATOR).atStartOf(e(VALUE_DECLARATION)))),
                params -> exposed(params, TypeOfExport.OPERATOR).map(OperatorDeclaration::parens));
        c.autocomplete(e().inside(e(VALUE_NAME)).inside(e(PATTERN_TERM).atStartOf(e(VALUE_DECLARATION))),
                params -> exposed(params, TypeOfExport.VALUE)
        );

        c.autocomplete(
                e().andOr(e().inside(e(OPERATOR)), childOf(RUNE_OF_AUTOCOMPLETION_EL)).inside(e(MODULE_HEADER)),
                ValueCompletion::moduleOperators
        );

        c.autocomplete(
                e().andOr(e().inside(e(VALUE_EXPORT)), childOf(RUNE_OF_AUTOCOMPLETION_EL)).inside(e(MODULE_HEADER)),
                ValueCompletion::moduleValues
        );

    }

    private static Stream<String> moduleOperators(CompletionParameters parameters) {
        return declared(parameters,
                TypeOfExport.OPERATOR,
                Declaration::declaredOperatorName).map(OperatorDeclaration::parens);
    }

    private static Stream<String> moduleValues(CompletionParameters parameters) {
        return declared(parameters, TypeOfExport.VALUE, Declaration::declaredValueNames);
    }

    private static Stream<String> declared(CompletionParameters parameters,
                                           TypeOfExport<?> typeOfExport,
                                           Function<Declaration, Stream<String>> valueExtractor) {
        Set<String> excluded = exposed(parameters, typeOfExport).collect(toSet());
        return module(parameters.getPosition()).declarations()
                                               .flatMap(valueExtractor)
                                               .flatMap(Streams::stream)
                                               .filter(o -> !excluded.contains(o));
    }

    private static Stream<String> exposed(CompletionParameters parameters,
                                          TypeOfExport<? extends PsiElement> exposedElementsType) {
        return module(parameters.getPosition())
                .exports(exposedElementsType)
                .map(PsiElement::getText);
    }
}
