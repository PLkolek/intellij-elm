package mkolaczek.elm.autocompletion;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;

public class Patterns {
    static PsiElementPattern.Capture<PsiElement> afterLeaf(PsiElementPattern.Capture<PsiElement> pattern) {
        return psiElement().afterLeafSkipping(whitespaceOrError(), pattern);
    }

    private static ElementPattern whitespaceOrError() {
        //noinspection unchecked
        return or(psiElement(TokenType.WHITE_SPACE), psiElement(PsiErrorElement.class));
    }

    static PsiElementPattern.Capture<PsiElement> justAfterLeaf(IElementType elementType) {
        return psiElement().afterLeaf(psiElement(elementType));
    }

    static PsiElementPattern.Capture<PsiElement> childOf(IElementType elementType) {
        return psiElement().withParent(psiElement(elementType));
    }
}
