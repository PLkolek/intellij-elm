package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import java.util.stream.Stream;

public class TypeConstructorReference extends ElmReference<PsiNamedElement> {
    public TypeConstructorReference(PsiNamedElement element) {
        super(element);
    }

    @Override
    protected Stream<? extends PsiElement> multiResolve() {
        return Resolver.forTypeConstructors().resolve(myElement);
    }

}