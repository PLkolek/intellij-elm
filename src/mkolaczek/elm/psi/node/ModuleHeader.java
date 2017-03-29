package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.PsiHasExposing;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class ModuleHeader extends ASTWrapperPsiElement implements PsiHasExposing {

    public ModuleHeader(ASTNode node) {
        super(node);
    }

    public ModuleName moduleName() {
        return findChildOfType(this, ModuleName.class);
    }


    @Override
    public boolean noExposingExposesAll() {
        return true;
    }
}
