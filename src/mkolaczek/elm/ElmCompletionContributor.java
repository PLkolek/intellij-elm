package mkolaczek.elm;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import mkolaczek.elm.psi.ElmTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;
import static mkolaczek.elm.psi.ElmTypes.*;

public class ElmCompletionContributor extends CompletionContributor {
    public ElmCompletionContributor() {
        autocompleteKeyword(justAfterLeaf(NEW_LINE), LookupElementBuilder.create("import"));
        autocompleteKeyword(afterLeaf(childOf(MODULE_NAME_REF)), LookupElementBuilder.create("as"), exposingCompletion());
        autocompleteKeyword(afterLeaf(childOf(MODULE_ALIAS)), exposingCompletion());
    }

    private void autocompleteKeyword(Capture<PsiElement> afterNewLine, final LookupElementBuilder... completions) {
        extend(CompletionType.BASIC, or(afterNewLine, psiElement(LOW_VAR)),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        for (LookupElementBuilder completion : completions) {
                            resultSet.addElement(completion);
                        }

                    }
                }
        );
    }

    private Capture<PsiElement> childOf(IElementType elementType) {
        return psiElement().withParent(psiElement(elementType));
    }

    private Capture<PsiElement> afterLeaf(Capture<PsiElement> pattern) {
        return psiElement().afterLeafSkipping(whitespaceOrError(), pattern);
    }

    private ElementPattern whitespaceOrError() {
        return or(psiElement(WHITE_SPACE), psiElement(PsiErrorElement.class));
    }

    private Capture<PsiElement> justAfterLeaf(IElementType elementType) {
        return psiElement().afterLeaf(psiElement(elementType));
    }

    @NotNull
    private LookupElementBuilder exposingCompletion() {
        return LookupElementBuilder.create("exposing").withInsertHandler(new ParenthesesInsertHandler<LookupElement>(true, false, true, true) {
            @Override
            protected boolean placeCaretInsideParentheses(InsertionContext context, LookupElement item) {
                return true;
            }

            @Nullable
            @Override
            protected PsiElement findNextToken(InsertionContext context) {
                final PsiFile file = context.getFile();
                PsiElement element = file.findElementAt(context.getTailOffset());
                if (element != null && isWhitespace(element)) {
                    element = file.findElementAt(element.getTextRange().getEndOffset());
                }
                return element;
            }

            private boolean isWhitespace(PsiElement element) {
                return element.getNode().getElementType() == ElmTypes.WHITE_SPACE || element.getNode().getElementType() == ElmTypes.NEW_LINE;
            }
        });
    }
}
