package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.DefinedValues;
import mkolaczek.elm.psi.node.ValueName;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfType;

public interface DefinesValues extends PsiElement {

    default Stream<DefinedValues> definedValues() {
        DefinedValues[] childrenOfType = getChildrenOfType(this, DefinedValues.class);
        return Arrays.stream(childrenOfType != null ? childrenOfType : new DefinedValues[]{});
    }

    default Stream<ValueName> declaredValues() {
        return definedValues().flatMap(DefinedValues::values);
    }
}
