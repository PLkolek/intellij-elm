package mkolaczek.elm.features.autocompletion;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Element;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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

    static PsiElementPattern.Capture<PsiElement> e(IElementType... types) {
        ElementPattern[] patterns = Arrays.stream(types).map(Patterns::e).toArray(ElementPattern[]::new);
        return e().andOr(patterns);
    }

    @NotNull
    static PsiElementPattern.Capture<? extends PsiElement> e(Class<? extends PsiElement> tClass) {
        return psiElement(tClass);
    }

    @NotNull
    static PsiElementPattern.Capture<PsiElement> e(IElementType type) {
        return psiElement(type);
    }

    static PsiElementPattern.Capture<PsiElement> afterLeaf(IElementType... elementType) {
        return afterLeaf(e(elementType));
    }

    @NotNull
    static PsiElementPattern.Capture<PsiElement> e() {
        return psiElement();
    }

    static PsiElementPattern.Capture<PsiElement> afterWhitespace(String wsChar) {
        return e().afterLeafSkipping(e(PsiErrorElement.class),
                e().withText(StandardPatterns.string().endsWith(wsChar)));
    }

    static PsiElementPattern.Capture<PsiElement> inside(Element element) {
        return e().inside(e(element));
    }

    static PsiElementPattern.Capture<PsiElement> after(Element element) {
        return e().inside(e().afterSibling(e(element)));
    }

    static PsiElementPattern.Capture<PsiElement> inBlock(Element block) {
        return e().andOr(inside(block), after(block));
    }

    static PsiElementPattern.Capture<PsiElement> inBlock(Element block, Element nextBlock) {
        return inBlock(block).andNot(inside(nextBlock));
    }
}
