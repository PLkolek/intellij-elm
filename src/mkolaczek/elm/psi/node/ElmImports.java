package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class ElmImports extends ASTWrapperPsiElement {

    public ElmImports(ASTNode node) {
        super(node);
    }
}
