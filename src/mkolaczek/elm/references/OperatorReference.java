package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.OperatorSymbolRef;

import java.util.stream.Stream;

public class OperatorReference extends ElmReference<OperatorSymbolRef> {
    public OperatorReference(OperatorSymbolRef element) {
        super(element);
    }


    @Override
    protected Stream<? extends PsiElement> multiResolve() {
        return Resolver.forOperators().resolve(myElement);
    }

}
