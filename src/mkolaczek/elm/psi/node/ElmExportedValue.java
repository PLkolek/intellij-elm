package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Optional;


public class ElmExportedValue extends ASTWrapperPsiElement implements PsiElement {
    public ElmExportedValue(ASTNode node) {
        super(node);
    }

    public Optional<ElmTypeExport> typeExport() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, ElmTypeExport.class));

    }
}
