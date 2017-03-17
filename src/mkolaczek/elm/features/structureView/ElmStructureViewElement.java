package mkolaczek.elm.features.structureView;

import com.google.common.base.Strings;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

public class ElmStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
    private final NavigatablePsiElement element;

    public ElmStructureViewElement(NavigatablePsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @Override
    public void navigate(boolean requestFocus) {
        element.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return element.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element.canNavigateToSource();
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return element instanceof PsiNamedElement ? Strings.nullToEmpty(element.getName()) : "";
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return Objects.requireNonNull(element.getPresentation());
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        if (element instanceof Module) {
            Module module = (Module) this.element;
            return Stream.concat(module.declarations(TypeOfDeclaration.TYPE),
                    module.declarations(TypeOfDeclaration.OPERATOR))
                         .map(ElmStructureViewElement::new)
                         .toArray(TreeElement[]::new);
        }
        if (element instanceof TypeDeclaration) {
            return ((TypeDeclaration) element).constructors()
                                              .map(ElmStructureViewElement::new)
                                              .toArray(TreeElement[]::new);
        }

        return EMPTY_ARRAY;
    }
}

