package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.references.QualifiedTypeReference;
import mkolaczek.elm.references.TypeReference;
import org.jetbrains.annotations.NotNull;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

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
        if (isQualified()) {
            return new QualifiedTypeReference(this);
        }
        return new TypeReference(this);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        if (!ModuleValueList.maybeDeleteChild(this)) {
            super.delete();
        }
    }

    private boolean isQualified() {
        return getParentOfType(this, QualifiedTypeNameRef.class) != null;
    }
}
