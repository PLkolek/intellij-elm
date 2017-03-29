package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;

public class DefinedValues extends ASTWrapperPsiElement {
    public DefinedValues(ASTNode node) {
        super(node);
    }

    public Stream<ValueName> values() {
        return findChildrenOfType(this, ValueName.class).stream();
    }

}
