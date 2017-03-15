package mkolaczek.elm.features.autocompletion;

import com.intellij.codeInsight.completion.AddSpaceInsertHandler;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.insertHandlers.BracesInsertHandler;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.features.autocompletion.ModulePattern.module;
import static mkolaczek.elm.features.autocompletion.Patterns.*;
import static mkolaczek.elm.features.autocompletion.insertHandlers.ElmParenthesesInsertHandler.parentheses;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class KeywordCompletion {
    static void keywords(ElmCompletionContributor contributor) {
        PsiElementPattern.Capture<PsiElement> insideEffectModule = e().inside(module().effectModule());

        contributor.autocomplete(
                onFreshLine().and(inBlock(IMPORTS, DECLARATIONS)),
                keyword("import")
        );
        contributor.autocomplete(
                afterLeaf(childOf(MODULE_NAME_REF)).andNot(onFreshLine()),
                keyword("as"), exposingCompletion()
        );
        contributor.autocomplete(
                afterLeaf(childOf(MODULE_NAME)).andNot(insideEffectModule),
                exposingCompletion()
        );
        contributor.autocomplete(
                afterLeaf(childOf(MODULE_NAME)).andOr(insideEffectModule, afterLeaf(insideEffectModule)),
                whereCompletion()
        );
        contributor.autocomplete(
                afterLeaf(RBRACKET).inside(module().effectModule()),
                exposingCompletion()
        );
        contributor.autocomplete(
                afterLeaf(childOf(MODULE_ALIAS)),
                exposingCompletion()
        );
        contributor.autocomplete(
                e().afterLeaf(e().isNull()),
                keyword("module"), keyword("port module"), keyword("effect module")
        );
        contributor.autocomplete(afterLeaf(PORT), keyword("module"));
        contributor.autocomplete(afterLeaf(EFFECT), keyword("module"));
        contributor.autocomplete(afterLeaf(TYPE), keyword("alias"));
        contributor.autocomplete(
                onFreshLine().and(after(IMPORTS)),
                keyword("type")
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
