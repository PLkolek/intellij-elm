package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class ValueName extends ASTWrapperPsiElement {
    public ValueName(ASTNode node) {
        super(node);
    }
}
