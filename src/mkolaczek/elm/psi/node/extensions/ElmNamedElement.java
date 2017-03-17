package mkolaczek.elm.psi.node.extensions;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class ElmNamedElement extends ASTWrapperPsiElement implements PsiNameIdentifierOwner {

    public ElmNamedElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public String getName() {
        return Optional.ofNullable(getNameIdentifier()).map(PsiElement::getText).orElse(null);
    }

    @NotNull
    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier != null) {
            if (nameIdentifier == this) {
                getNode().replaceAllChildrenToChildrenOf(createNewNameIdentifier(name).getNode());
            } else {
                nameIdentifier.replace(createNewNameIdentifier(name));
            }
        }
        return this;
    }

    protected abstract PsiElement createNewNameIdentifier(@NonNls @NotNull String name);

    @Override
    public int getTextOffset() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier != null ? nameIdentifier.getTextOffset() : super.getTextOffset();
    }
}
