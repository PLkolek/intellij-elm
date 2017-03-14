package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

public class ExposingNode extends ASTWrapperPsiElement {
    public ExposingNode(ASTNode node) {
        super(node);
    }

    public ModuleValueList valueList() {
        return PsiTreeUtil.findChildOfType(this, ModuleValueList.class);
    }
}
