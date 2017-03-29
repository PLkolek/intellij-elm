package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.references.ModuleReference;
import org.jetbrains.annotations.NotNull;

public class ModuleNameRef extends ASTWrapperPsiElement implements PsiNamedElement {

    public ModuleNameRef(ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public String getName() {
        return getNode().getText();
    }


    @Override
    public PsiReference getReference() {
        return new ModuleReference(this);
    }

    @Override
    public PsiElement setName(@NotNull String newElementName) {
        ASTNode newNode = ElmElementFactory.moduleNameRef(getProject(), newElementName).getNode();
        getNode().replaceAllChildrenToChildrenOf(newNode);
        return this;
    }

}