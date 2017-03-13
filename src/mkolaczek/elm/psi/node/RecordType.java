package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class RecordType extends ASTWrapperPsiElement {
    public RecordType(ASTNode node) {
        super(node);
    }
}
