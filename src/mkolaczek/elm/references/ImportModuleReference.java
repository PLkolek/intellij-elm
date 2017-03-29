package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.ModuleNameRef;

import java.util.stream.Stream;

public class ImportModuleReference extends ElmReference<ModuleNameRef> {
    public ImportModuleReference(ModuleNameRef element) {
        super(element);
    }

    @Override
    protected Stream<? extends PsiElement> multiResolve() {
        return ModuleResolver.resolveToModule(myElement);
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
