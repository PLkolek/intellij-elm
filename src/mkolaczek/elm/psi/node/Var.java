package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class Var extends ASTWrapperPsiElement {
    public Var(ASTNode node) {
        super(node);
    }
}
