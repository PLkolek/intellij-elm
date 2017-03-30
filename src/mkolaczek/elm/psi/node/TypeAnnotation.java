package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.Declaration;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static mkolaczek.elm.psi.PsiUtil.getChildOfType2;

public class TypeAnnotation extends ASTWrapperPsiElement implements Declaration {
    public TypeAnnotation(@NotNull ASTNode node) {
        super(node);
    }

    public Optional<Operator> operator() {
        return getChildOfType2(this, Operator.class);
    }

    public Optional<ValueNameRef> value() {
        return getChildOfType2(this, ValueNameRef.class);
    }

}
