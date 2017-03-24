package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import mkolaczek.elm.references.ValueReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValueExposing extends ElmNamedElement {
    public ValueExposing(ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return this;
    }

    @NotNull
    @Override
    protected PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.exposedValue(getProject(), name);
    }

    @Override
    public PsiReference getReference() {
        return new ValueReference(this);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        if (!ModuleValueList.maybeDeleteChild(this)) {
            super.delete();
        }
    }
}
