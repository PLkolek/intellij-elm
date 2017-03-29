package mkolaczek.elm.psi.node.extensions;

import mkolaczek.elm.psi.node.OperatorSymbolRef;
import mkolaczek.elm.psi.node.TypeExposing;
import mkolaczek.elm.psi.node.ValueExposing;
import mkolaczek.elm.references.Resolver;

public class TypeOfExposed {

    public static final TypeOfExposed TYPE = new TypeOfExposed(Resolver.forTypes(), TypeExposing.class);
    public static final TypeOfExposed OPERATOR = new TypeOfExposed(Resolver.forOperators(), OperatorSymbolRef.class);
    public static final TypeOfExposed VALUE = new TypeOfExposed(Resolver.forValues(), ValueExposing.class);

    private final Resolver<?> resolver;
    private final Class<? extends PsiExposed> psiClass;

    private TypeOfExposed(Resolver<?> resolver, Class<? extends PsiExposed> psiClass) {
        this.resolver = resolver;
        this.psiClass = psiClass;
    }


    public Class<? extends PsiExposed> psiClass() {
        return psiClass;
    }

    public Resolver<?> resolver() {
        return resolver;
    }
}
