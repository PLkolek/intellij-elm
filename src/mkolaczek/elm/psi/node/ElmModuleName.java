package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class ElmModuleName extends ASTWrapperPsiElement implements PsiElement {

    public ElmModuleName(ASTNode node) {
        super(node);
    }

}