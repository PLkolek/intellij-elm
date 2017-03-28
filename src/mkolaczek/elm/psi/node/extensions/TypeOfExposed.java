package mkolaczek.elm.psi.node.extensions;

import mkolaczek.elm.psi.node.OperatorSymbolRef;
import mkolaczek.elm.psi.node.TypeExposing;
import mkolaczek.elm.psi.node.ValueExposing;
import mkolaczek.elm.references.Resolver;

public class TypeOfExposed<T extends Exposed> {

    public static final TypeOfExposed<TypeExposing> TYPE = new TypeOfExposed<>(Resolver.forTypes(), TypeExposing.class);
    public static final TypeOfExposed<OperatorSymbolRef> OPERATOR = new TypeOfExposed<>(Resolver.forOperators(),
            OperatorSymbolRef.class);
    public static final TypeOfExposed<ValueExposing> VALUE = new TypeOfExposed<>(Resolver.forValues(),
            ValueExposing.class);

    private final Resolver<?> resolver;
    private final Class<T> psiClass;

    private TypeOfExposed(Resolver<?> resolver, Class<T> psiClass) {
        this.resolver = resolver;
        this.psiClass = psiClass;
    }


    public Class<T> psiClass() {
        return psiClass;
    }

    public Resolver<?> resolver() {
        return resolver;
    }
}
