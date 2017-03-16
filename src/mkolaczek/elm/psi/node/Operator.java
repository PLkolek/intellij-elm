package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.references.OperatorReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Operator extends ASTWrapperPsiElement implements PsiNameIdentifierOwner {


    public Operator(ASTNode node) {
        super(node);
    }

    public Optional<OperatorSymbol> symbol() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, OperatorSymbol.class));
    }

    @Override
    public PsiReference getReference() {
        return new OperatorReference(this);
    }


    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return symbol().orElse(null);
    }

    @Override
    @Nullable
    public String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier != null ? nameIdentifier.getText() : null;
    }

    @Override
    public PsiElement setName(@NotNull String newElementName) {
        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier != null) {
            return nameIdentifier.replace(ElmElementFactory.operatorSymbol(getProject(), newElementName));
        }
        return this;
    }
}
