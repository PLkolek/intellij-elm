package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.TypeNameRef;

import java.util.stream.Stream;

import static mkolaczek.elm.psi.PsiUtil.*;
import static mkolaczek.elm.psi.node.Module.module;
import static mkolaczek.elm.psi.node.extensions.TypeOfDeclaration.TYPE;

public class TypeReference extends ElmReference<TypeNameRef> {
    public TypeReference(TypeNameRef element) {
        super(element);
    }

    @Override
    protected Stream<? extends PsiElement> multiResolve() {
        Stream<? extends PsiNamedElement> resolved;
        if (insideModuleHeader(myElement)) {
            resolved = module(myElement).declarations(TYPE)
                                        .filter(d -> d.sameName(myElement.getName()));
        } else if (insideImport(myElement)) {
            resolved = containingImport(myElement).importedModule()
                                                  .flatMap(m -> m.exportedDeclaration(TYPE, myElement.getName()));
        } else {
            resolved = Resolver.forTypes().resolve(myElement);
        }
        return resolved;
    }
}
