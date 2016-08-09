package mkolaczek.elm.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

public class ElmModuleNameRef extends ASTWrapperPsiElement implements PsiElement {

    public ElmModuleNameRef(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ElmVisitor) {
            ((ElmVisitor) visitor).visitModuleNameRef(this);
        } else {
            super.accept(visitor);
        }
    }

    @Override
    public PsiReference getReference() {
        return new ElmModuleReference(this);
    }

    @Override
    public String getName() {
        return getNode().getText();
    }
}