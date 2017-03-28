package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;

public class EffectModuleSettingsList extends ASTWrapperPsiElement {
    public EffectModuleSettingsList(ASTNode node) {
        super(node);
    }

    public Collection<EffectModuleSetting> elements() {
        return PsiTreeUtil.findChildrenOfType(this, EffectModuleSetting.class);
    }
}
