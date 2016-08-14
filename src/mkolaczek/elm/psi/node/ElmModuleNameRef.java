package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import mkolaczek.elm.psi.ElmModuleReference;
import org.jetbrains.annotations.NotNull;

public class ElmModuleNameRef extends ASTWrapperPsiElement implements PsiElement {

    public ElmModuleNameRef(ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public String getName() {
        return getNode().getText();
    }


    @Override
    public PsiReference getReference() {
        return new ElmModuleReference(this);
    }
}