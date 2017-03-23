package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.QualifiedRef;

public class QualifiedTypeConstructorRef extends ASTWrapperPsiElement implements QualifiedRef {
    public QualifiedTypeConstructorRef(ASTNode node) {
        super(node);
    }
}
