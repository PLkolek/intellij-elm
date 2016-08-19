// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

public class ElmImport2 extends ASTWrapperPsiElement {

    public ElmImport2(ASTNode node) {
        super(node);
    }

    public ElmModuleNameRef importedModule() {
        return PsiTreeUtil.findChildOfType(this, ElmModuleNameRef.class);
    }
}
