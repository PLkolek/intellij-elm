package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

public class ElmModuleHeader extends ASTWrapperPsiElement {

    public ElmModuleHeader(ASTNode node) {
        super(node);
    }

    public ElmModuleName moduleName() {
        return PsiTreeUtil.findChildOfType(this, ElmModuleName.class);
    }
}
