package mkolaczek.elm.features.goTo;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.boilerplate.ElmIcon;
import mkolaczek.elm.psi.node.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ItemPresentation implements com.intellij.navigation.ItemPresentation {

    private final PsiElement element;

    public ItemPresentation(PsiElement element) {
        this.element = element;
    }

    @Nullable
    @Override
    public String getPresentableText() {
        if (element instanceof PsiNamedElement) {
            return ((PsiNamedElement) element).getName();
        }
        return "value declaration";
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
        } else if (element instanceof TypeDeclaration) {
            return ElmIcon.TYPE;
        } else if (element instanceof TypeConstructor) {
            return ElmIcon.CONSTRUCTOR;
        } else if (element instanceof OperatorDeclaration) {
            return ElmIcon.OPERATOR;
        } else if (element instanceof PortDeclaration) {
            return ElmIcon.PORT;
        } else if (element instanceof ValueDeclaration) {
            return ElmIcon.VALUE_DECLARATION;
        } else if (element instanceof ValueName) {
            return ElmIcon.VALUE;
        }

        return null;
    }
}
