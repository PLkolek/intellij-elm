package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.DefinedValues;
import mkolaczek.elm.psi.node.ValueName;

import java.util.stream.Stream;

import static mkolaczek.elm.psi.PsiUtil.getChildrenOfType2;

public interface DefinesValues extends PsiElement {

    default Stream<ValueName> declaredValues() {
        return getChildrenOfType2(this, DefinedValues.class).flatMap(DefinedValues::values);
    }
}
