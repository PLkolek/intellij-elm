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

    public BuiltInImport(String moduleName, String asName, boolean exposesAll, BuiltInExposed... exposedItems) {
        this.moduleName = moduleName;
        this.asName = asName;
        this.exposesAll = exposesAll;
        this.exposedItems = exposedItems;
    }

    public static AfterImport import_(String moduleName) {
        return new AfterImport(moduleName);
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

    static class AfterImport {
        private final String moduleName;

        private AfterImport(String moduleName) {
            this.moduleName = moduleName;
        }

        AfterAs as(String asName) {
            return new AfterAs(moduleName, asName);
        }

        BuiltInImport exposingAll() {
            return new BuiltInImport(moduleName, null, true);
        }

        BuiltInImport exposing(BuiltInExposed... names) {
            return new BuiltInImport(moduleName, null, false, names);
        }
    }

    static class AfterAs {
        private final String moduleName;
        private final String asName;

        private AfterAs(String moduleName, String asName) {
            this.moduleName = moduleName;
            this.asName = asName;
        }

        BuiltInImport exposingAll() {
            return new BuiltInImport(moduleName, asName, true);
        }

        BuiltInImport exposing(BuiltInExposed... exposed) {
            return new BuiltInImport(moduleName, asName, false, exposed);
        }
    }
}
