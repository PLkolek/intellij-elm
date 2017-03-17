package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.references.OperatorReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class OperatorSymbolRef extends ASTWrapperPsiElement implements PsiNamedElement {
    public OperatorSymbolRef(ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        return new OperatorReference(this);
    }

    @Override
    @Nullable
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(@NotNull String newElementName) {
        ASTNode operatorSymbol = ElmElementFactory.operatorSymbol(getProject(), newElementName).getNode();
        getNode().replaceAllChildrenToChildrenOf(operatorSymbol);
        return this;
    }
}
