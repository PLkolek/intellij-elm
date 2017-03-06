package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.ElmElementFactory;
import org.jetbrains.annotations.NotNull;

public class ElmTypeConstructor extends ASTWrapperPsiElement implements PsiNamedElement {
    public ElmTypeConstructor(ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(@NotNull String newElementName) {
        ASTNode newNode = ElmElementFactory.typeConstructor(getProject(), newElementName).getNode();
        getNode().replaceAllChildrenToChildrenOf(newNode);
        return this;
    }
}