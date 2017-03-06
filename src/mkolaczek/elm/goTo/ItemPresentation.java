package mkolaczek.elm.goTo;

import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.ElmIcon;
import mkolaczek.elm.psi.node.ElmModule;
import mkolaczek.elm.psi.node.ElmTypeConstructor;
import mkolaczek.elm.psi.node.ElmTypeName;
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
        if (element instanceof ElmModule) {
            return ElmIcon.FILE;
        } else if (element instanceof ElmTypeName) {
            return ElmIcon.TYPE;
        } else if (element instanceof ElmTypeConstructor) {
            return ElmIcon.CONSTRUCTOR;
        }
        return null;
    }
}
