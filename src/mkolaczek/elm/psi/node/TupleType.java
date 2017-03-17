package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.Surrounded;

public class TupleType extends ASTWrapperPsiElement implements Surrounded {
    public TupleType(ASTNode node) {
        super(node);
    }
}
