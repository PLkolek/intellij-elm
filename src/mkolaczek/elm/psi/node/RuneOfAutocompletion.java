package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class RuneOfAutocompletion extends ASTWrapperPsiElement {
    public RuneOfAutocompletion(ASTNode node) {
        super(node);
    }
}
