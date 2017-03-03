package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class ElmTypeDeclNode extends ASTWrapperPsiElement {
    public ElmTypeDeclNode(ASTNode node) {
        super(node);
    }
}
