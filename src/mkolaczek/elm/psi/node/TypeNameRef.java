package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.references.TypeReference;
import org.jetbrains.annotations.NotNull;

public class TypeNameRef extends ASTWrapperPsiElement implements PsiNamedElement {
    public TypeNameRef(ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public String getName() {
        return getNode().getText();
    }

    @Override
    public PsiElement setName(@NotNull String newElementName) {
        ASTNode newNode = ElmElementFactory.typeNameRef(getProject(), newElementName).getNode();
        getNode().replaceAllChildrenToChildrenOf(newNode);
        return this;
    }

    @Override
    public PsiReference getReference() {
        return new TypeReference(this);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        ExportedValue exportedValue = PsiTreeUtil.getParentOfType(this, ExportedValue.class);
        if (exportedValue != null) {
            exportedValue.containingListing().deleteChild(exportedValue);
        }
        super.delete();
    }
}
