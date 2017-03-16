package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionType;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.features.autocompletion.OperatorCompletionProvider;

import static mkolaczek.elm.features.autocompletion.Patterns.afterLeaf;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.INFIX_OPERATOR_DECLARATION;
import static mkolaczek.elm.psi.Tokens.DIGIT;

public class ValueCompletion {
    public static void values(ElmCompletionContributor c) {
        c.extend(CompletionType.BASIC,
                afterLeaf(e(DIGIT).inside(e(INFIX_OPERATOR_DECLARATION))),
                new OperatorCompletionProvider());
    }

}
