package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

public class ElmTypeExport extends ASTWrapperPsiElement {
    public ElmTypeExport(ASTNode node) {
        super(node);
    }

    public String typeName() {
        return PsiTreeUtil.firstChild(this).getText();
    }
}
