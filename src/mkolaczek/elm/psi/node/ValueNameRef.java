package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import mkolaczek.elm.references.ValueReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValueNameRef extends ElmNamedElement {
    public ValueNameRef(ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        return new ValueReference(this);
    }

    @NotNull
    @Override
    protected PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.valueNameRef(getProject(), name);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return this;
    }
}
