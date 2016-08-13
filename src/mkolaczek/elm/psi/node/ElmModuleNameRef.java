package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class ElmModuleNameRef extends ASTWrapperPsiElement implements PsiElement {

    public ElmModuleNameRef(ASTNode node) {
        super(node);
    }

}