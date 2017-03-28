package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import java.util.stream.Stream;

public class ValueReference extends ElmReference<PsiNamedElement> {
    public ValueReference(PsiNamedElement element) {
        super(element);
    }

    @Override
    protected Stream<? extends PsiElement> multiResolve() {
        return Resolver.forValues().resolve(myElement);
    }

}