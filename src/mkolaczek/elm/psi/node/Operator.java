package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Optional;

public class Operator extends ASTWrapperPsiElement {
    public Operator(ASTNode node) {
        super(node);
    }

    public Optional<OperatorSymbol> symbol() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, OperatorSymbol.class));
    }
}
