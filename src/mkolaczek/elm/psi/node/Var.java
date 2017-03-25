package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import mkolaczek.elm.references.TypeConstructorReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Var extends ElmNamedElement {
    public Var(ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        if (Character.isUpperCase(getText().charAt(0))) {
            return new TypeConstructorReference(this);
        }
        return null;
    }

    @NotNull
    @Override
    protected PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.var(getProject(), name);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return this;
    }
}
