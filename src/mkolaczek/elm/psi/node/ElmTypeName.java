package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class ElmTypeName extends ASTWrapperPsiElement {
    public ElmTypeName(ASTNode node) {
        super(node);
    }
}
