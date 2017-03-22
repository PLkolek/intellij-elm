package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class Expression extends ASTWrapperPsiElement {
    public Expression(ASTNode node) {
        super(node);
    }
}
