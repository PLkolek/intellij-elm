package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class OpenListing extends ASTWrapperPsiElement implements PsiElement {
    public OpenListing(ASTNode node) {
        super(node);
    }
}
