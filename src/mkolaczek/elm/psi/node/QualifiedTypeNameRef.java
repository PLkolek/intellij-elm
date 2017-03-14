package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

public class QualifiedTypeNameRef extends ASTWrapperPsiElement {
    public QualifiedTypeNameRef(ASTNode node) {
        super(node);
    }

    public ModuleNameRef moduleName() {
        return PsiTreeUtil.findChildOfType(this, ModuleNameRef.class);
    }
}
