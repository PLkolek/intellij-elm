package mkolaczek.elm.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class ElmModuleAlias extends ASTWrapperPsiElement implements PsiElement {

    public ElmModuleAlias(ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ElmVisitor) {
            ((ElmVisitor) visitor).visitPsiElement(this);
        } else {
            super.accept(visitor);
        }
    }

}
