package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class Term extends ASTWrapperPsiElement {
    public Term(ASTNode node) {
        super(node);
    }
}
