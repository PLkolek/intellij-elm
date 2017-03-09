// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.features.goTo.ItemPresentation;
import mkolaczek.elm.psi.node.extensions.DocCommented;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;

public class TypeDeclaration extends ASTWrapperPsiElement implements PsiNameIdentifierOwner, DocCommented {

    public TypeDeclaration(ASTNode node) {
        super(node);
    }

    public Collection<TypeConstructor> constructors() {
        return findChildrenOfType(this, TypeConstructor.class);
    }

    public boolean isAlias() {
        return PsiTreeUtil.findChildOfType(this, TypeAliasDeclNode.class) != null;
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        //required for ctrl+click find usages to work
        return PsiTreeUtil.findChildOfType(this, TypeName.class);
    }

    @Override
    @NotNull
    public String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier != null ? nameIdentifier.getText() : "";
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier != null) {
            return nameIdentifier.replace(ElmElementFactory.typeName(getProject(), name));
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

    @Override
    public Optional<DocComment> docComment() {
        PsiElement prevSibling = getPrevSibling();
        while (prevSibling instanceof PsiWhiteSpace) {
            prevSibling = prevSibling.getPrevSibling();
        }
        return prevSibling instanceof DocComment ? Optional.of((DocComment) prevSibling) : Optional.empty();
    }

    public Optional<TypeConstructor> constructor(String name) {
        return constructors().stream().filter(t -> name.equals(t.getName())).findAny();
    }
}
