package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class InfixOperatorDeclaration extends ASTWrapperPsiElement {
    public InfixOperatorDeclaration(ASTNode node) {
        super(node);
    }
}
