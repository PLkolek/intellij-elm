package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

public class TypeAliasDeclNode extends ASTWrapperPsiElement {
    public TypeAliasDeclNode(ASTNode node) {
        super(node);
    }

    public TypeDeclaration typeDeclaration() {
        return PsiTreeUtil.getParentOfType(this, TypeDeclaration.class);
    }
}
