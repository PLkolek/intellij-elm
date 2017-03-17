package mkolaczek.elm.features.inspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.refactoring.safeDelete.usageInfo.SafeDeleteReferenceUsageInfo;
import com.intellij.usageView.UsageInfo;
import mkolaczek.elm.features.refactoring.safeDelete.ElmSafeDeleteDelegate;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

class RemoveElementQuickFix implements LocalQuickFix {

    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Remove";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiNameIdentifierOwner toDelete = getParentOfType(descriptor.getPsiElement(),
                PsiNameIdentifierOwner.class,
                false);
        assert toDelete != null;
        for (UsageInfo usage : ElmSafeDeleteDelegate.usages(toDelete, new PsiElement[]{toDelete})) {
            if (usage instanceof SafeDeleteReferenceUsageInfo) {
                ((SafeDeleteReferenceUsageInfo) usage).deleteElement();
            }
        }
        toDelete.delete();
    }
}
