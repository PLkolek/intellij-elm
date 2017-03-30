package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.features.goTo.ItemPresentation;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import mkolaczek.elm.psi.node.extensions.HasTypeAnnotation;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PortDeclaration extends ElmNamedElement implements Declaration, HasTypeAnnotation {
    public PortDeclaration(ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return PsiTreeUtil.findChildOfType(this, PortName.class);
    }

    @NotNull
    @Override
    public PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.portName(getProject(), name);
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation(this);
    }

}
