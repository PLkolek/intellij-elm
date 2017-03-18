package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.PortDeclaration;
import mkolaczek.elm.psi.node.ValueExport;

import java.util.stream.Stream;

import static mkolaczek.elm.psi.PsiUtil.*;
import static mkolaczek.elm.psi.node.Module.module;
import static mkolaczek.elm.psi.node.extensions.TypeOfDeclaration.PORT;

public class ValueReference extends ElmReference<ValueExport> {
    public ValueReference(ValueExport element) {
        super(element);
    }

    @Override
    protected Stream<? extends PsiElement> multiResolve() {
        Stream<PortDeclaration> resolved;
        if (insideModuleHeader(myElement)) {
            resolved = module(myElement).declarations(PORT)
                                        .filter(d -> d.sameName(myElement.getName()));
        } else if (insideImport(myElement)) {
            resolved = containingImport(myElement).importedModule()
                                                  .flatMap(m -> m.exposedDeclaration(PORT, myElement.getName()));
        } else {
            throw new IllegalStateException("Operators in code not supperted yet");
        }
        return resolved;
    }

}