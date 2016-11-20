package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class EffectModuleSetting extends ASTWrapperPsiElement implements PsiElement {
    public EffectModuleSetting(ASTNode node) {
        super(node);
    }
}
