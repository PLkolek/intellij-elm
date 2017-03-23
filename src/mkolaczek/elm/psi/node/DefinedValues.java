package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class DefinedValues extends ASTWrapperPsiElement {
    public DefinedValues(ASTNode node) {
        super(node);
    }
}
