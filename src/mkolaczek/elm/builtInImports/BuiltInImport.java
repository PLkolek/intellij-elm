package mkolaczek.elm.builtInImports;

import mkolaczek.elm.psi.node.extensions.Exposed;
import mkolaczek.elm.psi.node.extensions.TypeOfExposed;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class BuiltInImport implements AbstractImport {

    private final String moduleName;
    private final String asName;
    private final boolean exposesAll;
    private final BuiltInExposed[] exposedItems;

    private BuiltInImport(String moduleName, String asName, boolean exposesAll, BuiltInExposed... exposedItems) {
        this.moduleName = moduleName;
        this.asName = asName;
        this.exposesAll = exposesAll;
        this.exposedItems = exposedItems;
    }

    static Builder import_(String moduleName) {
        return new Builder(moduleName);
    }

    public String moduleName() {
        return moduleName;
    }

    @Override
    public boolean exposesEverything() {
        return exposesAll;
    }

    @Override
    public Stream<Exposed> exposed(TypeOfExposed exposedType) {
        return Arrays.stream(exposedItems).filter(e -> e.type() == exposedType).map(x -> x);
    }

    public String importedAs() {
        return asName != null ? asName : moduleName;
    }

    @Override
    public Optional<String> moduleNameString() {
        return Optional.of(moduleName);
    }

    static class Builder {
        private final String moduleName;
        private String asName;

        private Builder(String moduleName) {
            this.moduleName = moduleName;
        }

        Builder as(String asName) {
            this.asName = asName;
            return this;
        }

        BuiltInImport exposingAll() {
            return new BuiltInImport(moduleName, asName, true);
        }

        BuiltInImport exposing(BuiltInExposed... names) {
            return new BuiltInImport(moduleName, asName, false, names);
        }
    }
}
