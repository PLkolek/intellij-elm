package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class ElmOpenListing extends ASTWrapperPsiElement implements PsiElement {
    public ElmOpenListing(ASTNode node) {
        super(node);
    }
}
