package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

public class ElmModuleName extends ASTWrapperPsiElement implements PsiElement {

    public ElmModuleName(ASTNode node) {
        super(node);
    }


    public ElmModule module() {
        return PsiTreeUtil.getParentOfType(this, ElmModule.class);
    }
}