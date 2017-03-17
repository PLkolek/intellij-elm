package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ModuleAlias extends ElmNamedElement {

    public ModuleAlias(ASTNode node) {
        super(node);
    }


    @NotNull
    @Override
    public PsiElement getNameIdentifier() {
        return this;
    }


    @Override
    public PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.moduleAlias(getProject(), name);
    }

    @Override
    public String getName() {
        return getNameIdentifier().getText();
    }
}
