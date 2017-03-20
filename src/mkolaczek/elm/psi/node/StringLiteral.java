package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class StringLiteral extends ASTWrapperPsiElement {
    public StringLiteral(ASTNode node) {
        super(node);
    }
}
