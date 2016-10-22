package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class ElmExposingNode extends ASTWrapperPsiElement implements PsiElement {
    public ElmExposingNode(ASTNode node) {
        super(node);
    }
}
