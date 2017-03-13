package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;


public class SurroundContents extends ASTWrapperPsiElement {
    public SurroundContents(ASTNode node) {
        super(node);
    }
}
