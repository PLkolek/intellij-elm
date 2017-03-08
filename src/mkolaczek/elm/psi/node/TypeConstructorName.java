package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class TypeConstructorName extends ASTWrapperPsiElement {
    public TypeConstructorName(ASTNode node) {
        super(node);
    }

}