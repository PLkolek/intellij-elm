package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class EffectModulePropertiesList extends ASTWrapperPsiElement implements PsiElement {
    public EffectModulePropertiesList(ASTNode node) {
        super(node);
    }
}
