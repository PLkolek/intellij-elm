package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

import java.util.Optional;

import static mkolaczek.elm.psi.PsiUtil.getChildOfType2;

public class TypeAnnotationEnd extends ASTWrapperPsiElement {
    public TypeAnnotationEnd(ASTNode node) {
        super(node);
    }

    public Optional<TypeExpression> typeExpression() {
        return getChildOfType2(this, TypeExpression.class);
    }
}
