package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.node.OperatorDeclaration;
import mkolaczek.elm.psi.node.extensions.Exposed;
import mkolaczek.elm.psi.node.extensions.HasExposing;
import mkolaczek.elm.psi.node.extensions.TypeOfExposed;
import mkolaczek.elm.references.Resolver;

import java.util.Set;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.features.autocompletion.Patterns.*;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;
import static mkolaczek.elm.psi.node.Module.module;

public class ValueCompletion {
    public static void values(ElmCompletionContributor c) {
        c.autocompletePlain(afterLeaf(e(DIGIT)).inside(e(INFIX_DECLARATION)),
                params -> exposed(params, TypeOfExposed.OPERATOR)
        );
        c.autocomplete(afterLeaf(e(PORT).inside(e(PORT_DECLARATION))), params -> exposed(params, TypeOfExposed.VALUE));
        c.autocomplete(e().atStartOf(e(VALUE_DECLARATION)), params -> exposed(params, TypeOfExposed.VALUE));
        c.autocomplete(e().atStartOf(e(VALUE_DECLARATION)),
                params -> exposed(params, TypeOfExposed.OPERATOR).map(OperatorDeclaration::parens));
        c.autocomplete(e().inside((e(OPERATOR).atStartOf(e(OPERATOR_DECLARATION)))),
                params -> exposed(params, TypeOfExposed.OPERATOR).map(OperatorDeclaration::parens));
        //noinspection unchecked
        c.autocomplete(e().andOr(e(RUNE_OF_AUTOCOMPLETION), e().inside(e(VALUE_NAME)))
                          .inside(e(PATTERN_TERM).atStartOf(e(VALUE_DECLARATION))),
                params -> exposed(params, TypeOfExposed.VALUE)
        );

        c.autocompletePlain(e().andOr(
                inside(OPERATOR_SYMBOL_REF).inside(e(EXPRESSION)),
                e().inside(e(QUALIFIED_VAR).withParent(e().afterSibling(e(TERM))))),
                ValueCompletion::visibleOperators
        );

        c.autocomplete(inExposing(OPERATOR), ValueCompletion::notExposedOperators);
        c.autocomplete(inExposing(VALUE_EXPOSING), ValueCompletion::notExposedValues);

        c.autocomplete(
                inside(EXPRESSION).afterLeaf(e(LPAREN)), p -> visibleOperators(p).map(OperatorDeclaration::parens)
        );

        c.autocomplete(e().inside(e(QUALIFIED_VAR)), ValueCompletion::visibleValues);

    }

    public static PsiElementPattern.Capture<PsiElement> inExposing(Element exposedItem) {
        return e().andOr(e().inside(e(exposedItem)), childOf(RUNE_OF_AUTOCOMPLETION_EL))
                  .inside(e(MODULE_HEADER, IMPORT_LINE));
    }

    private static Stream<String> notExposedOperators(CompletionParameters parameters) {
        return notExposed(Resolver.forOperators(), TypeOfExposed.OPERATOR, parameters)
                .map(OperatorDeclaration::parens);
    }

    private static Stream<String> notExposedValues(CompletionParameters parameters) {
        return notExposed(Resolver.forValues(), TypeOfExposed.VALUE, parameters);
    }

    private static Stream<String> notExposed(Resolver<?> resolver,
                                             TypeOfExposed<? extends Exposed> typeOfExposed,
                                             CompletionParameters parameters) {
        HasExposing hasExposing = getParentOfType(parameters.getPosition(), HasExposing.class);
        assert hasExposing != null;
        Set<String> exposed = hasExposing.exposed(typeOfExposed).map(Exposed::exposedName).collect(toSet());
        return resolver.variants(parameters.getPosition())
                       .filter(o -> !exposed.contains(o));
    }

    public static Stream<String> exposed(CompletionParameters parameters,
                                         TypeOfExposed<? extends Exposed> typeOfExposed) {
        return module(parameters.getPosition()).exposedNames(typeOfExposed);
    }

    private static Stream<String> visibleOperators(CompletionParameters parameters) {
        return Resolver.forOperators().variants(parameters.getPosition());
    }

    private static Stream<String> visibleValues(CompletionParameters parameters) {
        return Resolver.forValues().variants(parameters.getPosition());
    }

}
