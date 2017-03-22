package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class LetExpression extends ASTWrapperPsiElement {
    public LetExpression(ASTNode node) {
        super(node);
    }
}
