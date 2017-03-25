package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.elm.psi.node.extensions.DefinesValues;

import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;

public class ValueDeclaration extends ASTWrapperPsiElement implements Declaration, DefinesValues {
    public ValueDeclaration(ASTNode node) {
        super(node);
    }

    @Override
    public Stream<String> topLevelValueNames() {
        return topLevelValues().map(ValueName::getName);
    }

    public Stream<ValueName> topLevelValues() {
        return findChildrenOfType(this, MainDefinedValues.class).stream().flatMap(DefinedValues::values);
    }

}
