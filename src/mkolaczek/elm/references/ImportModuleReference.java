package mkolaczek.elm.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.psi.node.ModuleNameRef;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.psi.node.Module.module;

public class ImportModuleReference extends PsiReferenceBase.Poly<ModuleNameRef> {
    public ImportModuleReference(ModuleNameRef element) {
        super(element, TextRange.create(0, element.getTextLength()), false);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return ProjectUtil.modules(myElement.getProject())
                          .filter(module -> !module(myElement).sameName(module))
                          .toArray();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        return ModuleResolver.resolveToModule(myElement)
                             .map(PsiElementResolveResult::new)
                             .toArray(ResolveResult[]::new);
    }
}
