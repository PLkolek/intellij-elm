package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import mkolaczek.elm.psi.node.extensions.PsiExposed;
import mkolaczek.elm.references.OperatorReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;


public class OperatorSymbolRef extends ElmNamedElement implements PsiExposed {
    public OperatorSymbolRef(ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        return new OperatorReference(this);
    }


    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return this;
    }

    @Override
    public String exposedName() {
        return getName();
    }

    @Override
    public boolean exposes(String name) {
        return false;
    }

    @NotNull
    @Override
    protected PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.operatorNameRef(getProject(), name);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        Operator operator = getParentOfType(this, Operator.class);
        if (operator != null) {
            operator.delete();
        }
    }
}
