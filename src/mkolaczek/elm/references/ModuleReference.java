package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.ModuleNameRef;

import java.util.stream.Stream;

public class ModuleReference extends ElmReference<ModuleNameRef> {
    public ModuleReference(ModuleNameRef element) {
        super(element);
    }

    @Override
    protected Stream<? extends PsiElement> multiResolve() {
        return ModuleResolver.resolveToAlias(myElement);
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
