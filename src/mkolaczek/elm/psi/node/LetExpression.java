package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.DefinesValues;

public class LetExpression extends ASTWrapperPsiElement implements DefinesValues {
    public LetExpression(ASTNode node) {
        super(node);
    }
}
