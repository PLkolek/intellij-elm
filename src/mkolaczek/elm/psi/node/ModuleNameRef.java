package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.references.ImportModuleReference;
import mkolaczek.elm.references.ModuleReference;
import org.jetbrains.annotations.NotNull;

public class ModuleNameRef extends ASTWrapperPsiElement implements PsiNamedElement {

    public ModuleNameRef(ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public String getName() {
        return getNode().getText();
    }


    @Override
    public PsiReference getReference() {
        if (isImport()) {
            return new ImportModuleReference(this);
        }
        return new ModuleReference(this);
    }

    private boolean isImport() {
        return PsiTreeUtil.getParentOfType(this, Import.class) != null;
    }

    @Override
    public PsiElement setName(@NotNull String newElementName) {
        ASTNode newNode = ElmElementFactory.moduleNameRef(getProject(), newElementName).getNode();
        getNode().replaceAllChildrenToChildrenOf(newNode);
        return this;
    }

}