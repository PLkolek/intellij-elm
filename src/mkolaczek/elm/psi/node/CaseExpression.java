package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class CaseExpression extends ASTWrapperPsiElement {
    public CaseExpression(ASTNode node) {
        super(node);
    }
}
