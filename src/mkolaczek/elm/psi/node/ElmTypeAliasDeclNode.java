package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class ElmTypeAliasDeclNode extends ASTWrapperPsiElement {
    public ElmTypeAliasDeclNode(ASTNode node) {
        super(node);
    }
}
