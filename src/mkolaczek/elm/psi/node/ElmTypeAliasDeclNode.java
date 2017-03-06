package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

public class ElmTypeAliasDeclNode extends ASTWrapperPsiElement {
    public ElmTypeAliasDeclNode(ASTNode node) {
        super(node);
    }

    public ElmTypeDeclaration typeDeclaration() {
        return PsiTreeUtil.getParentOfType(this, ElmTypeDeclaration.class);
    }
}
