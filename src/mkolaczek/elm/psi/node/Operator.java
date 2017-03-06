package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class Operator extends ASTWrapperPsiElement implements PsiElement {
    public Operator(ASTNode node) {
        super(node);
    }
}
