package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

public class ElmExposingNode extends ASTWrapperPsiElement implements PsiElement {
    public ElmExposingNode(ASTNode node) {
        super(node);
    }

    public ElmModuleValueList valueList() {
        return PsiTreeUtil.findChildOfType(this, ElmModuleValueList.class);
    }
}
