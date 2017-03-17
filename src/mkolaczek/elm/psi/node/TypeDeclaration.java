// This is a generated file. Not intended for manual editing.
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
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;

public class TypeDeclaration extends ElmNamedElement implements DocCommented {

    public TypeDeclaration(ASTNode node) {
        super(node);
    }

    public Stream<TypeConstructor> constructors() {
        return findChildrenOfType(this, TypeConstructor.class).stream();
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
    public PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.typeName(getProject(), name);
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation(this);
    }

    public Optional<TypeConstructor> constructor(String name) {
        return constructors().filter(t -> name.equals(t.getName())).findAny();
    }
}
