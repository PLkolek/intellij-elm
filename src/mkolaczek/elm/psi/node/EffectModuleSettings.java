package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class EffectModuleSettings extends ASTWrapperPsiElement implements PsiElement {
    public EffectModuleSettings(ASTNode node) {
        super(node);
    }
}
