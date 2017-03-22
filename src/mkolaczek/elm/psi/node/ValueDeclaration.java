package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class ValueDeclaration extends ASTWrapperPsiElement {
    public ValueDeclaration(ASTNode node) {
        super(node);
    }
}
