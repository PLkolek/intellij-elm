package mkolaczek.elm.syntaxHinglighting;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.DocComment;
import mkolaczek.elm.psi.node.MultilineComment;
import org.jetbrains.annotations.NotNull;

public class ElmAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof DocComment) {
            createAnnotation(element, holder, ElmSyntaxHighlighter.COMMENT);
        } else if (element instanceof MultilineComment) {
            createAnnotation(element, holder, ElmSyntaxHighlighter.DOC_COMMENT);
        }
    }

    private void createAnnotation(@NotNull PsiElement element,
                                  @NotNull AnnotationHolder holder, TextAttributesKey textAttributes) {
        Annotation infoAnnotation = holder.createInfoAnnotation(element, null);
        infoAnnotation.setTextAttributes(textAttributes);
    }
}
