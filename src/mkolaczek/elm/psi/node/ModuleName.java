package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

public class ModuleName extends ASTWrapperPsiElement {

    public ModuleName(ASTNode node) {
        super(node);
    }


    public Module module() {
        return PsiTreeUtil.getParentOfType(this, Module.class);
    }
}