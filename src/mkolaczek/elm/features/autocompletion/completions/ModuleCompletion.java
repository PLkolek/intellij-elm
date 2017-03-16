package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.features.autocompletion.Names;
import mkolaczek.elm.features.autocompletion.Patterns;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.ModuleNameRef;
import mkolaczek.elm.psi.node.QualifiedTypeNameRef;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static mkolaczek.elm.features.autocompletion.ElmCompletionContributor.location;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.QUALIFIED_TYPE_NAME_REF;
import static mkolaczek.elm.psi.node.Module.module;

public class ModuleCompletion {
    public static void modules(ElmCompletionContributor c) {
        c.autocomplete(Patterns.afterLeaf(Tokens.MODULE), ModuleCompletion::fileName);
        c.autocomplete(Patterns.afterLeaf(Tokens.AS), ModuleCompletion::moduleNameParts);
        c.autocomplete(e().inside(e(QUALIFIED_TYPE_NAME_REF)), ModuleCompletion::modules);
    }

    private static Stream<String> modules(CompletionParameters parameters) {
        QualifiedTypeNameRef qualifiedType = location(parameters, QualifiedTypeNameRef.class);
        String prefix = qualifiedType.moduleName().map(ModuleNameRef::getName).orElse("");
        String finalPrefix = prefix.length() > 0 ? prefix + "." : "";

        return ProjectUtil.otherModuleNames(qualifiedType.getProject(), module(qualifiedType))
                          .map(n -> Names.suffix(n, finalPrefix))
                          .filter(n -> !n.isEmpty());
    }

    @NotNull
    private static Stream<String> moduleNameParts(CompletionParameters parameters) {
        return location(parameters, Import.class).importedModuleName()
                                                 .map(m -> m.getName().split("\\."))
                                                 .map(Names::suggest)
                                                 .orElse(Stream.empty());
    }

    @NotNull
    private static Stream<String> fileName(CompletionParameters parameters) {
        String fileName = parameters.getOriginalFile().getName();
        String moduleName = fileName.substring(0, fileName.length() - 4);//cut out .elm
        return Stream.of(moduleName);
    }
}
