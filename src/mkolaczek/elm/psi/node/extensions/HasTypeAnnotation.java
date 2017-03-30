package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.TypeExpression;

import java.util.Optional;

import static mkolaczek.elm.psi.PsiUtil.getChildOfType2;

public interface HasTypeAnnotation extends PsiElement {

    default Optional<TypeExpression> typeExpression() {
        return getChildOfType2(this, TypeExpression.class);
    }
}
