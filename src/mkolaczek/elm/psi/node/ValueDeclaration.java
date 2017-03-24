package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.elm.psi.node.extensions.DefinesValues;
import mkolaczek.util.Streams;

import java.util.stream.Stream;

public class ValueDeclaration extends ASTWrapperPsiElement implements Declaration, DefinesValues {
    public ValueDeclaration(ASTNode node) {
        super(node);
    }

    @Override
    public Stream<String> declaredValueNames() {
        return definedValues().flatMap(DefinedValues::valueNames);
    }

    @Override
    public Stream<String> declaredOperatorName() {
        return definedValues().map(DefinedValues::operatorName).flatMap(Streams::stream);
    }
}
