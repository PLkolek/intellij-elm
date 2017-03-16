package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.features.autocompletion.Names;
import mkolaczek.elm.features.autocompletion.Patterns;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.ModuleNameRef;
import mkolaczek.elm.psi.node.QualifiedTypeNameRef;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static mkolaczek.elm.features.autocompletion.ElmCompletionContributor.location;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.QUALIFIED_TYPE_NAME_REF;

public class ModuleCompletion {
    public static void modules(ElmCompletionContributor c) {
        c.autocomplete(Patterns.afterLeaf(Tokens.MODULE), ModuleCompletion::fileName);
        c.autocomplete(Patterns.afterLeaf(Tokens.AS), ModuleCompletion::moduleNameParts);
        c.autocomplete(e().inside(e(QUALIFIED_TYPE_NAME_REF)), ModuleCompletion::modules);
    }

    private static Stream<String> modules(CompletionParameters parameters) {
        QualifiedTypeNameRef qualifiedType = location(parameters, QualifiedTypeNameRef.class);
        String prefix = qualifiedType.moduleName().map(ModuleNameRef::getName).orElse("");

        return ProjectUtil.modules(qualifiedType.getProject())
                          .map(Module::getName)
                          .filter(n -> n.startsWith(prefix))
                          .map(n -> Names.suffix(n, prefix))
                          .filter(n -> !n.isEmpty());
    }

    @NotNull
    private static Stream<String> moduleNameParts(CompletionParameters parameters) {
        Import importLine = location(parameters, Import.class);
        ModuleNameRef module = importLine.importedModuleName();
        String[] words = module.getName().split("\\.");
        return Names.suggest(words).stream();
    }

    @NotNull
    private static Stream<String> fileName(CompletionParameters parameters) {
        String fileName = parameters.getOriginalFile().getName();
        String moduleName = fileName.substring(0, fileName.length() - 4);//cut out .elm
        return Stream.of(moduleName);
    }
}
