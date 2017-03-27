package mkolaczek.elm.features.refactoring;


import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.ValueName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElmRefactoringSupportProvider extends RefactoringSupportProvider {

    @Override
    public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement element, @Nullable PsiElement context) {
        return true;
    }

    @Override
    public boolean isSafeDeleteAvailable(@NotNull PsiElement element) {
        return !(element instanceof ValueName);
    }
}
