package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.AddSpaceInsertHandler;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.features.autocompletion.insertHandlers.BracesInsertHandler;
import mkolaczek.elm.psi.Elements;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static mkolaczek.elm.features.autocompletion.ModulePattern.module;
import static mkolaczek.elm.features.autocompletion.Patterns.*;
import static mkolaczek.elm.features.autocompletion.insertHandlers.ElmParenthesesInsertHandler.parentheses;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class KeywordCompletion {
    public static void keywords(ElmCompletionContributor c) {
        PsiElementPattern.Capture<PsiElement> inEffectModule = e().inside(module().effectModule());
        //@formatter:off
        c.autocomplete(onFreshLine().andOr(e().inside(e(IMPORTS)), e().atStartOf(e(DECLARATIONS))),   keyword("import"));
        c.autocomplete(afterLeaf(childOf(MODULE_NAME)).andNot(inEffectModule),                  exposingCompletion());
        c.autocomplete(afterLeaf(RBRACKET).and(inEffectModule),                                 exposingCompletion());
        c.autocomplete(afterLeaf(childOf(MODULE_NAME)).and(inEffectModule),                     whereCompletion());
        c.autocomplete(afterLeaf(childOf(MODULE_ALIAS)),                                        exposingCompletion());
        c.autocomplete(afterLeaf(PORT).inside(e(MODULE_HEADER)),                                keyword("module"));
        c.autocomplete(afterLeaf(EFFECT),                                                       keyword("module"));
        c.autocomplete(afterLeaf(TYPE),                                                         keyword("alias"));
        c.autocomplete(onFreshLine().and(after(IMPORTS)),                                       keyword("type"));
        c.autocomplete(onFreshLine().and(after(IMPORTS)),                                       keyword("infixr"));
        c.autocomplete(onFreshLine().and(after(IMPORTS)),                                       keyword("infixl"));
        c.autocomplete(onFreshLine().and(after(IMPORTS)),                                       keyword("infix"));
        c.autocomplete(onFreshLine().and(after(IMPORTS)),                                       keyword("port"));

        c.autocomplete(e().atStartOf(e(EXPRESSION)),                                            keyword("let"));
        c.autocomplete(inside(EXPRESSION).afterLeaf(e(LPAREN)),                                 keyword("let"));
        c.autocomplete(e().atStartOf(e(OPERAND)),                                               keyword("let"));
        c.autocomplete(e().inside(e(LET_EXPRESSION)),                                           keyword("in")); //too broad, but...

        c.autocomplete(e().atStartOf(e(EXPRESSION)),                                            keyword("if"));
        c.autocomplete(inside(EXPRESSION).afterLeaf(e(LPAREN)),                                 keyword("if"));
        c.autocomplete(e().atStartOf(e(OPERAND)),                                               keyword("if"));
        c.autocomplete(e().inside(e(Elements.IF_EXPRESSION)),                                   keyword("then")); //too broad, but...
        c.autocomplete(e().inside(e(IF_EXPRESSION)),                                            keyword("else")); //too broad, but...

        c.autocomplete(e().atStartOf(e(EXPRESSION)),                                            keyword("case"));
        c.autocomplete(inside(EXPRESSION).afterLeaf(e(LPAREN)),                                 keyword("case"));
        c.autocomplete(e().atStartOf(e(OPERAND)),                                               keyword("case"));
        c.autocomplete(e().inside(e(CASE_EXPRESSION)),                                          keyword("of")); //too broad, but...
        //@formatter:on

        c.autocomplete(afterLeaf(childOf(MODULE_NAME_REF).inside(e(IMPORT_LINE))).andNot(onFreshLine()),
                keyword("as"), exposingCompletion()
        );
        c.autocomplete(e().afterLeaf(e().isNull()), keywords("module", "port module", "effect module"));
    }

    private static LookupElementBuilder whereCompletion() {
        return LookupElementBuilder.create("where").withInsertHandler(new BracesInsertHandler());
    }

    @NotNull
    private static LookupElementBuilder exposingCompletion() {
        return LookupElementBuilder.create("exposing").withInsertHandler(parentheses());
    }

    @NotNull
    private static LookupElementBuilder keyword(String item) {
        return LookupElementBuilder.create(item).withInsertHandler(AddSpaceInsertHandler.INSTANCE);
    }

    @NotNull
    private static LookupElementBuilder[] keywords(String... items) {
        return Arrays.stream(items).map(KeywordCompletion::keyword).toArray(LookupElementBuilder[]::new);
    }

}
