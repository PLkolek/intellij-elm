package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ModuleAlias extends ASTWrapperPsiElement implements PsiNameIdentifierOwner {

    public ModuleAlias(ASTNode node) {
        super(node);
    }


    @NotNull
    @Override
    public PsiElement getNameIdentifier() {
        return this;
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        ASTNode newNode = ElmElementFactory.moduleAlias(getProject(), name).getNode();
        getNode().replaceAllChildrenToChildrenOf(newNode);
        return this;
    }

    @Override
    public String getName() {
        return getNameIdentifier().getText();
    }
}
