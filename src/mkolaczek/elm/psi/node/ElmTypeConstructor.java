package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class ElmTypeConstructor extends ASTWrapperPsiElement {
    public ElmTypeConstructor(ASTNode node) {
        super(node);
    }
}
