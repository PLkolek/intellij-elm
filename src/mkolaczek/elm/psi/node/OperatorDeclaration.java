package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.features.goTo.ItemPresentation;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.elm.psi.node.extensions.DefinesValues;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import mkolaczek.util.Streams;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

import static mkolaczek.elm.psi.node.Module.module;

public class OperatorDeclaration extends ElmNamedElement implements Declaration, DefinesValues {
    public OperatorDeclaration(ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public OperatorSymbol getNameIdentifier() {
        //required for ctrl+click find usages to work
        return PsiTreeUtil.findChildOfType(this, OperatorSymbol.class);
    }

    @NotNull
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

    public boolean sameParensName(String name) {
        return name.equals(parensName().orElse(null));
    }

    @Override
    public Stream<String> declaredOperatorName() {
        return Streams.stream(Optional.ofNullable(getName()));
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation(this);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        module(this).declarations(TypeOfDeclaration.INFIX, getName()).forEach(PsiElement::delete);
        super.delete();
    }
}
