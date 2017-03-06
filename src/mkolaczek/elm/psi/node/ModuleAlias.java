package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class ModuleAlias extends ASTWrapperPsiElement implements PsiElement {

    public ModuleAlias(ASTNode node) {
        super(node);
    }


}
