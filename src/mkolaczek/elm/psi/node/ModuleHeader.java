package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.HasExposing;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class ModuleHeader extends ASTWrapperPsiElement implements HasExposing {

    public ModuleHeader(ASTNode node) {
        super(node);
    }

    public ModuleName moduleName() {
        return findChildOfType(this, ModuleName.class);
    }


}
