package mkolaczek.elm.builtInImports;

import com.intellij.util.ArrayUtil;

public class BuiltInImport {

    private final String moduleName;
    private final String asName;
    private final boolean exposesAll;
    private final String[] names;

    public BuiltInImport(String moduleName, String asName, boolean exposesAll, String[] names) {
        this.moduleName = moduleName;
        this.asName = asName;
        this.exposesAll = exposesAll;
        this.names = names;
    }

    public static AfterImport import_(String moduleName) {
        return new AfterImport(moduleName);
    }

    public String moduleName() {
        return moduleName;
    }

    public boolean exposesAll() {
        return exposesAll;
    }

    public String[] exposedNames() {
        return names;
    }

    public String importedAs() {
        return asName != null ? asName : moduleName;
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
            return new BuiltInImport(moduleName, null, true, ArrayUtil.EMPTY_STRING_ARRAY);
        }

        BuiltInImport exposing(String... names) {
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
            return new BuiltInImport(moduleName, asName, true, ArrayUtil.EMPTY_STRING_ARRAY);
        }

        BuiltInImport exposing(String... names) {
            return new BuiltInImport(moduleName, asName, false, names);
        }
    }
}
