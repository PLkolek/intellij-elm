package mkolaczek.elm.psi.node;


import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static mkolaczek.elm.psi.PsiUtil.findChildrenOfType2;

public class TypeExpression extends ASTWrapperPsiElement {
    public TypeExpression(@NotNull ASTNode node) {
        super(node);
    }

    public Stream<QualifiedTypeNameRef> typeRefs() {
        return findChildrenOfType2(this, QualifiedTypeNameRef.class);
    }

    public Stream<TypeVariable> typeVariables() {
        return findChildrenOfType2(this, TypeVariable.class);
    }

}
