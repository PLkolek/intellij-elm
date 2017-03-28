package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.TypeNameRef;

import java.util.stream.Stream;

public class TypeReference extends ElmReference<TypeNameRef> {
    public TypeReference(TypeNameRef element) {
        super(element);
    }

    @Override
    protected Stream<? extends PsiElement> multiResolve() {
        return Resolver.forTypes().resolve(myElement);
    }
}
