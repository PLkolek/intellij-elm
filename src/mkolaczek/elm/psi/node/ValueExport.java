package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class ValueExport extends ASTWrapperPsiElement {
    public ValueExport(ASTNode node) {
        super(node);
    }
}
