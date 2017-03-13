package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class TypeConstructorArgs extends ASTWrapperPsiElement {
    public TypeConstructorArgs(ASTNode node) {
        super(node);
    }
}
