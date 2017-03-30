package mkolaczek.elm.builtInImports;

import mkolaczek.elm.psi.node.extensions.Exposed;
import mkolaczek.elm.psi.node.extensions.TypeOfExposed;

class BuiltInExposed implements Exposed {

    private final TypeOfExposed type;
    private final String name;
    private final boolean exposesAll;


    private BuiltInExposed(TypeOfExposed type, String name, boolean exposesAll) {
        this.type = type;
        this.name = name;
        this.exposesAll = exposesAll;
    }

    static BuiltInExposed operator(String name) {
        return new BuiltInExposed(TypeOfExposed.OPERATOR, name, false);

    }

    static BuiltInExposed openType(String name) {
        return new BuiltInExposed(TypeOfExposed.TYPE, name, true);
    }

    static BuiltInExposed closedType(String name) {
        return new BuiltInExposed(TypeOfExposed.TYPE, name, false);
    }

    public TypeOfExposed type() {
        return type;
    }

    @Override
    public String exposedName() {
        return name;
    }

    @Override
    public boolean exposes(String name) {
        return exposesAll;
    }
}
