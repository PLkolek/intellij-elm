package mkolaczek.elm.unresolvedReferences;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import mkolaczek.elm.findUsages.ElmFindUsagesProvider;
import org.jetbrains.annotations.NotNull;

public class UnresolvedReferenceAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        PsiReference ref = element.getReference();
        if (ref != null && ref.resolve() == null) {
            holder.createErrorAnnotation(element, "Unresolved " + ElmFindUsagesProvider.type(element));
        }
    }
}
