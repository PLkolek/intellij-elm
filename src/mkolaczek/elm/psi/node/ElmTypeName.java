package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ElmTypeName extends ASTWrapperPsiElement implements PsiNamedElement {
    public ElmTypeName(ASTNode node) {
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
}
