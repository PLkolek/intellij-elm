package mkolaczek.elm.psi.node;

import com.google.common.base.Preconditions;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.features.goTo.ItemPresentation;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import mkolaczek.elm.psi.node.extensions.SeparatedList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

public class TypeConstructor extends ElmNamedElement {
    public TypeConstructor(ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        //required for ctrl+click find usages to work
        return PsiTreeUtil.findChildOfType(this, TypeConstructorName.class);
    }


    @NotNull
    @Override
    public PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.typeConstructor(getProject(), name);
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation(this);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        this.containingListing().deleteSeparator(this);
        super.delete();
    }

    private SeparatedList containingListing() {
        return getParentOfType(this, PipeSeparatedList.class);
    }

    @NotNull
    public TypeDeclaration typeDeclaration() {
        return Preconditions.checkNotNull(getParentOfType(this, TypeDeclaration.class));
    }
}
