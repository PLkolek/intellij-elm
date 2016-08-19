package mkolaczek.elm;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.ElmDocComment;
import mkolaczek.elm.psi.node.ElmMultilineComment;
import mkolaczek.elm.syntaxHinglighting.ElmSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

public class ElmAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof ElmDocComment) {
            Annotation infoAnnotation = holder.createInfoAnnotation(element, null);
            infoAnnotation.setTextAttributes(ElmSyntaxHighlighter.COMMENT);
        } else if (element instanceof ElmMultilineComment) {
            Annotation infoAnnotation = holder.createInfoAnnotation(element, null);
            infoAnnotation.setTextAttributes(ElmSyntaxHighlighter.DOC_COMMENT);
        }

    }
}
