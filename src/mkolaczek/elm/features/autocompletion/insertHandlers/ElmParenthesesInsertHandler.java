package mkolaczek.elm.features.autocompletion.insertHandlers;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;

public class ElmParenthesesInsertHandler extends ParenthesesInsertHandler<LookupElement> {
    public ElmParenthesesInsertHandler() {
        super(true, false, true, true);
    }

    @Override
    protected boolean placeCaretInsideParentheses(InsertionContext context,
                                                  LookupElement item) {
        return true;
    }

    public static ElmParenthesesInsertHandler parentheses() {
        return new ElmParenthesesInsertHandler();
    }
}