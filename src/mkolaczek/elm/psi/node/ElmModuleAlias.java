package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class ElmModuleAlias extends ASTWrapperPsiElement implements PsiElement {

    public ElmModuleAlias(ASTNode node) {
        super(node);
    }


}
