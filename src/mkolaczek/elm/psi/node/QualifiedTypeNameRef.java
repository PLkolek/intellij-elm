package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.QualifiedRef;

public class QualifiedTypeNameRef extends ASTWrapperPsiElement implements QualifiedRef {
    public QualifiedTypeNameRef(ASTNode node) {
        super(node);
    }
}
