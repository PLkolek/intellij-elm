package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;


public class ElmExportedValue extends ASTWrapperPsiElement implements PsiElement {
    public ElmExportedValue(ASTNode node) {
        super(node);
    }
}
