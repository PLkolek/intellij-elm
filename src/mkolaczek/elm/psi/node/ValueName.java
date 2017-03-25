package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.features.goTo.ItemPresentation;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValueName extends ElmNamedElement {
    public ValueName(ASTNode node) {
        super(node);
    }

    @NotNull
    @Override
    protected PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.valueName(getProject(), name);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return this;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation(this);
    }
}
