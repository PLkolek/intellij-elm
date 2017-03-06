package mkolaczek.elm.goTo;

import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.ElmIcon;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeName;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ItemPresentation implements com.intellij.navigation.ItemPresentation {

    private final PsiNamedElement element;

    public ItemPresentation(PsiNamedElement element) {
        this.element = element;
    }

    @Nullable
    @Override
    public String getPresentableText() {
        return element.getName();
    }

    @Nullable
    @Override
    public String getLocationString() {
        return element.getContainingFile().getName();
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
        if (element instanceof Module) {
            return ElmIcon.FILE;
        } else if (element instanceof TypeName) {
            return ElmIcon.TYPE;
        } else if (element instanceof TypeConstructor) {
            return ElmIcon.CONSTRUCTOR;
        }
        return null;
    }
}
