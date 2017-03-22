package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class Operand extends ASTWrapperPsiElement {
    public Operand(ASTNode node) {
        super(node);
    }
}
