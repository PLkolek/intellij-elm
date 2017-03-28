package mkolaczek.elm.references;

import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.project.Project;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.psi.node.Module;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public class DefaultImports {
    private static final Set<String> MODULES = ImmutableSet.of("Basics",
            "Debug",
            "List",
            "Maybe",
            "Result",
            "String",
            "Tuple",
            "Platform",
            "Platform.Cmd",
            "Platform.Sub");


    public static Stream<Module> modules(Project project) {
        return ProjectUtil.modules(project, MODULES);
    }

    public static Collection<String> moduleNames() {
        return MODULES;
    }
}
