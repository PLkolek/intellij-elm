package mkolaczek.elm.psi.node;

import com.google.common.base.Preconditions;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.references.OperatorReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;


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

    @NotNull
    public Operator operator() {
        return Preconditions.checkNotNull(getParentOfType(this, Operator.class));
    }

    @Override
    public void delete() throws IncorrectOperationException {
        operator().delete();
    }
}
