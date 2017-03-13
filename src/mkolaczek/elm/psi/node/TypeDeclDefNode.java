package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class TypeDeclDefNode extends ASTWrapperPsiElement {
    public TypeDeclDefNode(@NotNull ASTNode node) {
        super(node);
    }
}
