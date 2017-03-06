// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

public class Import extends ASTWrapperPsiElement {

    public Import(ASTNode node) {
        super(node);
    }

    public ModuleNameRef importedModule() {
        return PsiTreeUtil.findChildOfType(this, ModuleNameRef.class);
    }
}
