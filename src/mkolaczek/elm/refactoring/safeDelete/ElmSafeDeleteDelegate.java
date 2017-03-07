package mkolaczek.elm.refactoring.safeDelete;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.RefactoringSettings;
import com.intellij.refactoring.safeDelete.NonCodeUsageSearchInfo;
import com.intellij.refactoring.safeDelete.SafeDeleteProcessor;
import com.intellij.refactoring.safeDelete.SafeDeleteProcessorDelegate;
import com.intellij.refactoring.safeDelete.usageInfo.SafeDeleteReferenceSimpleDeleteUsageInfo;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmLanguage;
import mkolaczek.elm.psi.node.ExposingNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class ElmSafeDeleteDelegate implements SafeDeleteProcessorDelegate {
    @Override
    public boolean handlesElement(PsiElement element) {
        return element.getLanguage() == ElmLanguage.INSTANCE;
    }

    @Nullable
    @Override
    public NonCodeUsageSearchInfo findUsages(@NotNull PsiElement element,
                                             @NotNull PsiElement[] allElementsToDelete,
                                             @NotNull List<UsageInfo> result) {
        if (element instanceof PsiNamedElement) {
            ReferencesSearch.search(element).forEach(reference -> {
                final PsiElement refElement = reference.getElement();
                if (!isInside(refElement, allElementsToDelete)) {
                    result.add(new SafeDeleteReferenceSimpleDeleteUsageInfo(refElement,
                            element,
                            PsiTreeUtil.getParentOfType(refElement,
                                    ExposingNode.class) != null));
                }
                return true;
            });
        }
        return new NonCodeUsageSearchInfo(SafeDeleteProcessor.getDefaultInsideDeletedCondition(allElementsToDelete),
                element);
    }

    private boolean isInside(PsiElement refElement, PsiElement[] ancestors) {
        for (PsiElement element : ancestors) {
            if (SafeDeleteProcessor.isInside(refElement, element)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public Collection<? extends PsiElement> getElementsToSearch(@NotNull PsiElement element,
                                                                @NotNull Collection<PsiElement> allElementsToDelete) {
        return allElementsToDelete;
    }

    @Nullable
    @Override
    public Collection<PsiElement> getAdditionalElementsToDelete(@NotNull PsiElement element,
                                                                @NotNull Collection<PsiElement> allElementsToDelete,
                                                                boolean askUser) {
        return null;
    }

    @Nullable
    @Override
    public Collection<String> findConflicts(@NotNull PsiElement element, @NotNull PsiElement[] allElementsToDelete) {
        return null;
    }

    @Nullable
    @Override
    public UsageInfo[] preprocessUsages(Project project, UsageInfo[] usages) {
        return usages;
    }

    @Override
    public void prepareForDeletion(PsiElement element) throws IncorrectOperationException {

    }

    @Override
    public boolean isToSearchInComments(PsiElement element) {
        return RefactoringSettings.getInstance().SAFE_DELETE_SEARCH_IN_COMMENTS;
    }

    @Override
    public void setToSearchInComments(PsiElement element, boolean enabled) {
        RefactoringSettings.getInstance().SAFE_DELETE_SEARCH_IN_COMMENTS = enabled;
    }

    @Override
    public boolean isToSearchForTextOccurrences(PsiElement element) {
        return RefactoringSettings.getInstance().SAFE_DELETE_SEARCH_IN_NON_JAVA;
    }

    @Override
    public void setToSearchForTextOccurrences(PsiElement element, boolean enabled) {
        RefactoringSettings.getInstance().SAFE_DELETE_SEARCH_IN_NON_JAVA = enabled;
    }
}
