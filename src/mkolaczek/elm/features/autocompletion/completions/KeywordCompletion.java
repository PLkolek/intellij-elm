package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.AddSpaceInsertHandler;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.features.autocompletion.insertHandlers.BracesInsertHandler;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.features.autocompletion.ModulePattern.module;
import static mkolaczek.elm.features.autocompletion.Patterns.*;
import static mkolaczek.elm.features.autocompletion.insertHandlers.ElmParenthesesInsertHandler.parentheses;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class KeywordCompletion {
    public static void keywords(ElmCompletionContributor c) {
        PsiElementPattern.Capture<PsiElement> inEffectModule = e().inside(module().effectModule());
        //@formatter:off
        //temporary fix for definition parsing
        c.autocomplete(onFreshLine().andOr(inBlock(IMPORTS), inBlock(DECLARATIONS).isFirstAcceptedChild(e())),       keyword("import"));
        c.autocomplete(afterLeaf(childOf(MODULE_NAME)).andNot(inEffectModule),  exposingCompletion());
        c.autocomplete(afterLeaf(RBRACKET).and(inEffectModule),                 exposingCompletion());
        c.autocomplete(afterLeaf(childOf(MODULE_NAME)).and(inEffectModule),     whereCompletion());
        c.autocomplete(afterLeaf(childOf(MODULE_ALIAS)),                        exposingCompletion());
        c.autocomplete(afterLeaf(PORT).inside(e(MODULE_HEADER)),                keyword("module"));
        c.autocomplete(afterLeaf(EFFECT),                                       keyword("module"));
        c.autocomplete(afterLeaf(TYPE),                                         keyword("alias"));
        c.autocomplete(onFreshLine().and(after(IMPORTS)),                       keyword("type"));
        c.autocomplete(onFreshLine().and(after(IMPORTS)),                       keyword("infixr"));
        c.autocomplete(onFreshLine().and(after(IMPORTS)),                       keyword("infixl"));
        c.autocomplete(onFreshLine().and(after(IMPORTS)),                       keyword("infix"));
        c.autocomplete(onFreshLine().and(after(IMPORTS)),                       keyword("port"));

        c.autocomplete(e().atStartOf(e(EXPRESSION)),                            keyword("let"));
        //@formatter:on

        c.autocomplete(afterLeaf(childOf(MODULE_NAME_REF).inside(e(IMPORT_LINE))).andNot(onFreshLine()),
                keyword("as"), exposingCompletion()
        );
        c.autocomplete(
                e().afterLeaf(e().isNull()),
                keyword("module"), keyword("port module"), keyword("effect module")
        );
    }

    private static LookupElementBuilder whereCompletion() {
        return LookupElementBuilder.create("where")
                                   .withInsertHandler(new BracesInsertHandler());
    }


    @NotNull
    private static LookupElementBuilder exposingCompletion() {
        return LookupElementBuilder.create("exposing").withInsertHandler(parentheses());
    }

    @NotNull
    private static LookupElementBuilder keyword(String item) {
        return LookupElementBuilder.create(item).withInsertHandler(AddSpaceInsertHandler.INSTANCE);
    }

}
