package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.util.IncorrectOperationException;

public class Operator extends ASTWrapperPsiElement {


    public Operator(ASTNode node) {
        super(node);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        if (!ModuleValueList.maybeDeleteChild(this)) {
            super.delete();
        }
    }
}
