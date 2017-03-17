package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.features.goTo.ItemPresentation;
import mkolaczek.elm.psi.node.extensions.DocCommented;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OperatorDeclaration extends ElmNamedElement implements DocCommented {
    public OperatorDeclaration(ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public OperatorSymbol getNameIdentifier() {
        //required for ctrl+click find usages to work
        return PsiTreeUtil.findChildOfType(this, OperatorSymbol.class);
    }

    @Override
    public PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.operatorName(getProject(), name);
    }

    public Optional<String> parensName() {
        return Optional.ofNullable(getName()).map(OperatorDeclaration::parens);
    }

    @NotNull
    public static String parens(String n) {
        return "(" + n + ")";
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation(this);
    }

    public boolean sameName(String name) {
        return getName() != null && getName().equals(name);
    }

    public boolean sameParensName(String name) {
        return name.equals(parensName().orElse(null));
    }
}
