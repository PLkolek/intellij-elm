package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.goTo.ItemPresentation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TypeConstructor extends ASTWrapperPsiElement implements PsiNamedElement, PsiNameIdentifierOwner {
    public TypeConstructor(ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(@NotNull String newElementName) {
        ASTNode newNode = ElmElementFactory.typeConstructor(getProject(), newElementName).getNode();
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