package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValueExport extends ElmNamedElement {
    public ValueExport(ASTNode node) {
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
        return ElmElementFactory.valueExport(getProject(), name);
    }
}
