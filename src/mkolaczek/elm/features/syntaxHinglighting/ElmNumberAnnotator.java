package mkolaczek.elm.features.syntaxHinglighting;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

public class ElmNumberAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        IElementType elementType = element.getNode().getElementType();
        if (elementType == Tokens.INVALID_HEX_NUMBER) {
            holder.createErrorAnnotation(element, "Hexadecimal number must contain at least one hexadecimal digit");
        }
    }
}
