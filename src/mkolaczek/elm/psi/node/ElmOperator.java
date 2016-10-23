package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class ElmOperator extends ASTWrapperPsiElement implements PsiElement {
    public ElmOperator(ASTNode node) {
        super(node);
    }
}
