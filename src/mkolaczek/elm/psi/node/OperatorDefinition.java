package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.util.Streams;

import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static java.util.Optional.ofNullable;

public class OperatorDefinition extends ASTWrapperPsiElement implements Declaration {
    public OperatorDefinition(ASTNode node) {
        super(node);
    }

    @Override
    public Stream<String> declaredOperatorName() {
        return Streams.stream(ofNullable(findChildOfType(this, OperatorSymbol.class)).map(OperatorSymbol::getText));
    }
}
