package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ElmModuleName extends ASTWrapperPsiElement implements PsiElement, PsiNamedElement {

    public ElmModuleName(ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        return getNode().getText();
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        getNode().replaceAllChildrenToChildrenOf(ElmElementFactory.moduleName(getProject(), name).getNode());
        return this;
    }
}