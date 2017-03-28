package mkolaczek.elm.psi.node.extensions;

import mkolaczek.elm.psi.node.OperatorSymbolRef;
import mkolaczek.elm.psi.node.TypeExposing;
import mkolaczek.elm.psi.node.ValueExposing;

public interface TypeOfExposed<T extends Exposed> {

    Class<T> psiClass();

    TypeOfExposed<TypeExposing> TYPE = () -> TypeExposing.class;
    TypeOfExposed<OperatorSymbolRef> OPERATOR = () -> OperatorSymbolRef.class;
    TypeOfExposed<ValueExposing> VALUE = () -> ValueExposing.class;
}
