package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.util.Streams;

import java.util.Optional;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getChildOfType;

public class ValueDeclaration extends ASTWrapperPsiElement implements Declaration {
    public ValueDeclaration(ASTNode node) {
        super(node);
    }


    public Optional<DefinedValues> definedValues() {
        return Optional.ofNullable(getChildOfType(this, DefinedValues.class));
    }

    public Stream<ValueName> declaredValues() {
        return Streams.stream(definedValues()).flatMap(DefinedValues::values);
    }

    @Override
    public Stream<String> declaredValueNames() {
        return Streams.stream(definedValues()).flatMap(DefinedValues::valueNames);
    }

    @Override
    public Stream<String> declaredOperatorName() {
        return Streams.stream(definedValues().flatMap(DefinedValues::operatorName));
    }
}
