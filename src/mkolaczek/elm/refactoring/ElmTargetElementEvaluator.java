package mkolaczek.elm.refactoring;


import com.intellij.codeInsight.TargetElementEvaluatorEx2;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.ModuleName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElmTargetElementEvaluator extends TargetElementEvaluatorEx2 {
    @Nullable
    @Override
    public PsiElement getElementByReference(@NotNull PsiReference ref, int flags) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getNamedElement(@NotNull PsiElement element) {
        if (element instanceof LeafPsiElement) {
            LeafPsiElement leaf = (LeafPsiElement) element;
            if (PsiTreeUtil.getParentOfType(leaf, ModuleName.class) != null) {
                return PsiTreeUtil.getParentOfType(element, Module.class);
            }
        }
        return super.getNamedElement(element);
    }
}
