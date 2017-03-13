package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class TupleType extends ASTWrapperPsiElement {
    public TupleType(ASTNode node) {
        super(node);
    }
}
