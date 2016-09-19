package mkolaczek.elm.psi.node;


import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElmModule extends ASTWrapperPsiElement implements PsiElement, PsiNamedElement, PsiNameIdentifierOwner {
    public ElmModule(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return PsiTreeUtil.findChildOfType(this, ElmModuleName.class);
    }

    @Override
    public String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier != null ? nameIdentifier.getText() : ""; //null means no header line
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier != null) {
            return nameIdentifier.replace(ElmElementFactory.moduleName(getProject(), name));
        }
        return this;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        getContainingFile().delete();
    }

    @Override
    public int getTextOffset() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier != null ? nameIdentifier.getTextOffset() : super.getTextOffset();
    }
}
