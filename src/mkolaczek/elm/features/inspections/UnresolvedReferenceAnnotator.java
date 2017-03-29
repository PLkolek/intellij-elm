package mkolaczek.elm.features.inspections;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import mkolaczek.elm.builtInImports.BuiltInImports;
import mkolaczek.elm.features.ElmFindUsagesProvider;
import mkolaczek.elm.psi.node.ModuleNameRef;
import mkolaczek.elm.psi.node.TypeNameRef;
import org.jetbrains.annotations.NotNull;

public class UnresolvedReferenceAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        PsiReference ref = element.getReference();
        if ((element instanceof TypeNameRef && isBuiltIn((TypeNameRef) element)) || resolves(ref)) {
            return;
        }
        if (element instanceof ModuleNameRef) {
            String message = "Unresolved " + ElmFindUsagesProvider.type(element);
            if (BuiltInImports.moduleNames().anyMatch(m -> m.equals(((ModuleNameRef) element).getName()))) {
                message = message + ". Have you run \"elm-package install\"?";
            }
            Annotation annotation = holder.createErrorAnnotation(element, message);

            String moduleNameToImport = ((ModuleNameRef) element).getName();
            annotation.registerFix(new AddImportIntentionAction(moduleNameToImport));
        } else {
            String message = "Unresolved " + ElmFindUsagesProvider.type(element);
            holder.createErrorAnnotation(element, message);
        }
    }

    private boolean isBuiltIn(@NotNull TypeNameRef element) {
        return BuiltInImports.typeNames().contains(element.getName());
    }

    private boolean resolves(PsiReference ref) {
        if (ref == null) {
            return true;
        }
        if (ref instanceof PsiPolyVariantReference) {
            return ((PsiPolyVariantReference) ref).multiResolve(false).length > 0;
        }
        return ref.resolve() != null;
    }
}
