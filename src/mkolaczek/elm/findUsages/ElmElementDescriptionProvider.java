package mkolaczek.elm.findUsages;


import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElmElementDescriptionProvider implements ElementDescriptionProvider {
    @Nullable
    @Override
    public String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
        return ((PsiNamedElement) element).getName();
    }
}
