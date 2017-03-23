package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

import java.util.Optional;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;
import static java.util.Optional.ofNullable;

public class DefinedValues extends ASTWrapperPsiElement {
    public DefinedValues(ASTNode node) {
        super(node);
    }

    public Optional<String> operatorName() {
        return ofNullable(findChildOfType(this, OperatorSymbol.class)).map(OperatorSymbol::getText);
    }

    public Stream<String> valueNames() {
        return findChildrenOfType(this, ValueName.class).stream().map(ValueName::getName);
    }

}
