package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

//just for autocompletion of operators...
public class BinaryOperator extends ASTWrapperPsiElement {
    public BinaryOperator(@NotNull ASTNode node) {
        super(node);
    }
}
