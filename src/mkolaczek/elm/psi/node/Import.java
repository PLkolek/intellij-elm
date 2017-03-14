// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.node.extensions.HasExposing;

public class Import extends HasExposing {

    public Import(ASTNode node) {
        super(node);
    }

    public ModuleNameRef importedModule() {
        return PsiTreeUtil.findChildOfType(this, ModuleNameRef.class);
    }
}
