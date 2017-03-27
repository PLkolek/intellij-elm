package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import java.util.stream.Stream;

import static mkolaczek.elm.psi.PsiUtil.containingImport;
import static mkolaczek.elm.psi.PsiUtil.insideImport;
import static mkolaczek.elm.psi.node.extensions.TypeOfDeclaration.PORT;

public class ValueReference extends ElmReference<PsiNamedElement> {
    public ValueReference(PsiNamedElement element) {
        super(element);
    }

    @Override
    protected Stream<? extends PsiElement> multiResolve() {
        Stream<? extends PsiElement> resolved;
        if (insideImport(myElement)) {
            resolved = containingImport(myElement).importedModule()
                                                  .flatMap(m -> m.exportedDeclaration(PORT, myElement.getName()));
        } else {
            resolved = Resolver.forValues().resolve(myElement);
        }
        return resolved;
    }

}