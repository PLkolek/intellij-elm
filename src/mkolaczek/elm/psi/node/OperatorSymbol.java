package mkolaczek.elm.psi.node;


import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class OperatorSymbol extends ASTWrapperPsiElement {
    public OperatorSymbol(@NotNull ASTNode node) {
        super(node);
    }


}
