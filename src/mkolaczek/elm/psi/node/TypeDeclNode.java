package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class TypeDeclNode extends ASTWrapperPsiElement {
    public TypeDeclNode(ASTNode node) {
        super(node);
    }
}
