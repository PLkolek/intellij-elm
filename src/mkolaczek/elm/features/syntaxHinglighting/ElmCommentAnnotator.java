package mkolaczek.elm.features.syntaxHinglighting;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.DocComment;
import org.jetbrains.annotations.NotNull;

public class ElmCommentAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof DocComment) {
            Annotation infoAnnotation = holder.createInfoAnnotation(element, null);
            infoAnnotation.setTextAttributes(DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        }
    }

}
