package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.references.TypeConstructorReference;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

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

    @Override
    public void delete() throws IncorrectOperationException {
        this.containingListing().deleteSeparator(this);
        super.delete();
    }

    private CommaSeparatedList containingListing() {
        return checkNotNull(PsiTreeUtil.getParentOfType(this, CommaSeparatedList.class));
    }
}
