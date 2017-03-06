package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.references.TypeConstructorReference;
import org.jetbrains.annotations.NotNull;

public class TypeConstructorRef extends ASTWrapperPsiElement implements PsiNamedElement {
    public TypeConstructorRef(ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public String getName() {
        return getNode().getText();
    }

    @Override
    public PsiElement setName(@NotNull String newElementName) {
        ASTNode newNode = ElmElementFactory.typeConstructorRef(getProject(), newElementName).getNode();
        getNode().replaceAllChildrenToChildrenOf(newNode);
        return this;
    }

    @Override
    public PsiReference getReference() {
        return new TypeConstructorReference(this);
    }
}
