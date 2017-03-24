package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.DefinesValues;

public class LambdaExpression extends ASTWrapperPsiElement implements DefinesValues {
    public LambdaExpression(ASTNode node) {
        super(node);
    }
}
