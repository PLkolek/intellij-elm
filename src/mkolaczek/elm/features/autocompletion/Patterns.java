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
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.RUNE_OF_AUTOCOMPLETION;

public class Patterns {
    public static PsiElementPattern.Capture<PsiElement> afterLeaf(PsiElementPattern.Capture<PsiElement> pattern) {
        return psiElement().afterLeafSkipping(whitespaceOrError(), pattern);
    }

    private static ElementPattern whitespaceOrError() {
        //noinspection unchecked
        return or(psiElement(TokenType.WHITE_SPACE), psiElement(PsiErrorElement.class));
    }

    public static PsiElementPattern.Capture<PsiElement> childOf(IElementType elementType) {
        return psiElement().withParent(psiElement(elementType));
    }

    public static PsiElementPattern.Capture<PsiElement> e(IElementType... types) {
        ElementPattern[] patterns = Arrays.stream(types).map(Patterns::e).toArray(ElementPattern[]::new);
        return e().andOr(patterns);
    }

    @NotNull
    private static PsiElementPattern.Capture<? extends PsiElement> e(Class<? extends PsiElement> tClass) {
        return psiElement(tClass);
    }

    @NotNull
    public static PsiElementPattern.Capture<PsiElement> e(IElementType type) {
        return psiElement(type);
    }

    public static PsiElementPattern.Capture<PsiElement> afterLeaf(IElementType... elementType) {
        return afterLeaf(e(elementType));
    }

    @NotNull
    public static PsiElementPattern.Capture<PsiElement> e() {
        return psiElement();
    }

    public static PsiElementPattern.Capture<PsiElement> onFreshLine() {
        return e().afterLeafSkipping(e(PsiErrorElement.class),
                e().withText(StandardPatterns.string().endsWith("\n")));
    }

    public static PsiElementPattern.Capture<PsiElement> inside(Element element) {
        return e().inside(e(element));
    }

    public static PsiElementPattern.Capture<PsiElement> after(Element element) {
        return e().inside(e().afterSibling(e(element)));
    }

    public static PsiElementPattern.Capture<PsiElement> inExposing(Element exposedItem) {
        return e().andOr(inside(exposedItem), e(RUNE_OF_AUTOCOMPLETION).andNot(inside(TYPE_CONSTRUCTOR_REF)))
                  .inside(e(MODULE_HEADER, IMPORT_LINE));
    }
}
