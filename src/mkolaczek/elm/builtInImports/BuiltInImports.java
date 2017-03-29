package mkolaczek.elm.builtInImports;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static mkolaczek.elm.builtInImports.BuiltInExposed.*;
import static mkolaczek.elm.builtInImports.BuiltInImport.import_;

public class BuiltInImports {
    private static final Set<BuiltInImport> MODULES = ImmutableSet.of(
            import_("Basics").exposingAll(),
            import_("Debug").exposing(),
            import_("List").exposing(operator("::")),
            import_("Maybe").exposing(openType("Maybe")),
            import_("Result").exposing(openType("Result")),
            import_("String").exposing(),
            import_("Tuple").exposing(),
            import_("Platform").exposing(closedType("Program")),
            import_("Platform.Cmd").as("Cmd").exposing(closedType("Cmd"), operator("!")),
            import_("Platform.Sub").as("Sub").exposing(closedType("Sub"))
    );

    private static final Set<String> TYPES = ImmutableSet.of(
            "List",
            "Int",
            "Float",
            "Char",
            "String",
            "Bool"
    );


    public static Stream<String> moduleNames() {
        return imports().map(BuiltInImport::moduleName);
    }

    public static Collection<String> typeNames() {
        return TYPES;
    }

    public static Stream<BuiltInImport> imports() {
        return MODULES.stream();
    }
}
