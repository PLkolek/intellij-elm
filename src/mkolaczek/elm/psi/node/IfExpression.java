package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class IfExpression extends ASTWrapperPsiElement {
    public IfExpression(ASTNode node) {
        super(node);
    }
}
