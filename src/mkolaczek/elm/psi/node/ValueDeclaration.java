package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.elm.psi.node.extensions.DefinesValues;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfType;

public class ValueDeclaration extends ASTWrapperPsiElement implements Declaration, DefinesValues {
    public ValueDeclaration(ASTNode node) {
        super(node);
    }

    public Stream<String> topLevelValueNames() {
        return topLevelValues().map(ValueName::getName);
    }

    public Stream<ValueName> topLevelValues() {
        DefinedValues beforeEquals = beforeEquals();
        if (beforeEquals == null) {
            return Stream.empty();
        }
        MainDefinedValues[] values = getChildrenOfType(beforeEquals, MainDefinedValues.class);
        values = values == null ? new MainDefinedValues[]{} : values;
        return Arrays.stream(values).flatMap(DefinedValues::values);
    }


    @Override
    public ItemPresentation getPresentation() {
        return new mkolaczek.elm.features.goTo.ItemPresentation(this);
    }

    public DefinedValues beforeEquals() {
        return getChildOfType(this, DefinedValues.class);
    }
}
