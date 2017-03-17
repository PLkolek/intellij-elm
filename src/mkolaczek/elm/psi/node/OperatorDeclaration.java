package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.features.goTo.ItemPresentation;
import mkolaczek.elm.psi.node.extensions.DocCommented;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OperatorDeclaration extends ASTWrapperPsiElement implements PsiNameIdentifierOwner, DocCommented {
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
    @Nullable
    public String getName() {
        return name().orElse(null);
    }

    public Optional<String> parensName() {
        return name().map(OperatorDeclaration::parens);
    }

    @NotNull
    public static String parens(String n) {
        return "(" + n + ")";
    }

    private Optional<String> name() {
        return Optional.ofNullable(getNameIdentifier()).map(PsiElement::getText);
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier != null) {
            return nameIdentifier.replace(ElmElementFactory.operatorName(getProject(), name));
        }
        return this;
    }

    @Override
    public int getTextOffset() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier != null ? nameIdentifier.getTextOffset() : super.getTextOffset();
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
