package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.Declaration;
import org.jetbrains.annotations.NotNull;

public class TypeAnnotation extends ASTWrapperPsiElement implements Declaration {
    public TypeAnnotation(@NotNull ASTNode node) {
        super(node);
    }

}
