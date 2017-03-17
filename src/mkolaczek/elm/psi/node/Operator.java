package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;

import java.util.Optional;

public class Operator extends ASTWrapperPsiElement {


    public Operator(ASTNode node) {
        super(node);
    }

    public Optional<OperatorSymbolRef> symbol() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, OperatorSymbolRef.class));
    }

    @Override
    public void delete() throws IncorrectOperationException {
        if (!ModuleValueList.maybeDeleteChild(this)) {
            super.delete();
        }
    }
}
