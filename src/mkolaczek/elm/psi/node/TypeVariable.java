package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class TypeVariable extends ASTWrapperPsiElement {
    public TypeVariable(ASTNode node) {
        super(node);
    }
}
