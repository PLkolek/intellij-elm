package mkolaczek.elm.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.ModuleNameRef;
import mkolaczek.util.Streams;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.psi.node.Module.module;

public class ModuleReference extends PsiReferenceBase.Poly<ModuleNameRef> {
    public ModuleReference(ModuleNameRef element) {
        super(element, TextRange.create(0, element.getTextLength()), false);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        return module(myElement)
                .aliasedImports(myElement.getName())
                .stream()
                .map(Import::alias)
                .flatMap(Streams::stream)
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
