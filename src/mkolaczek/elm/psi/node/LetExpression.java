package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.DefinesValues;

import java.util.stream.Stream;

import static mkolaczek.elm.psi.PsiUtil.getChildrenOfType2;

public class LetExpression extends ASTWrapperPsiElement implements DefinesValues {
    public LetExpression(ASTNode node) {
        super(node);
    }

    @Override
    public Stream<ValueName> declaredValues() {
        return getChildrenOfType2(this, ValueDeclaration.class).flatMap(DefinesValues::declaredValues);
    }
}
