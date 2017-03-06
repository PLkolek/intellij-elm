package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.goTo.ItemPresentation;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TypeName extends ASTWrapperPsiElement implements PsiNamedElement, PsiNameIdentifierOwner {
    public TypeName(ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        ASTNode newNode = ElmElementFactory.typeName(getProject(), name).getNode();
        getNode().replaceAllChildrenToChildrenOf(newNode);
        return this;
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        //required for ctrl+click find usages to work
        return this;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation(this);
    }
}
