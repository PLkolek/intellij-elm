package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.features.autocompletion.Names;
import mkolaczek.elm.features.autocompletion.Patterns;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.ModuleNameRef;
import mkolaczek.elm.psi.node.extensions.QualifiedRef;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static mkolaczek.elm.features.autocompletion.ElmCompletionContributor.location;
import static mkolaczek.elm.features.autocompletion.Patterns.e;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.RUNE_OF_AUTOCOMPLETION;
import static mkolaczek.elm.psi.node.Module.module;

public class ModuleCompletion {
    public static void modules(ElmCompletionContributor c) {
        c.autocomplete(Patterns.afterLeaf(Tokens.MODULE), ModuleCompletion::fileName);
        c.autocomplete(Patterns.afterLeaf(Tokens.AS), ModuleCompletion::moduleNameParts);
        c.autocomplete(e().inside(e(QUALIFIED_TYPE_NAME_REF)), ModuleCompletion::modules);
        c.autocomplete(e().inside(e(QUALIFIED_TYPE_CONSTRUCTOR_REF)), ModuleCompletion::modules);
        c.autocomplete(e().inside(e(QUALIFIED_VAR)), ModuleCompletion::modules);
        c.autocomplete(e(RUNE_OF_AUTOCOMPLETION).inside(e(PATTERN_TERM)),
                params -> matchingModules(params.getPosition(), "")
        );
    }

    private static Stream<String> modules(CompletionParameters parameters) {
        QualifiedRef qualifiedType = location(parameters, QualifiedRef.class);
        String prefix = qualifiedType.moduleName().map(ModuleNameRef::getName).orElse("");
        String finalPrefix = prefix.length() > 0 ? prefix + "." : "";

        return matchingModules(qualifiedType, finalPrefix);
    }

    private static Stream<String> matchingModules(PsiElement location, String finalPrefix) {
        return ProjectUtil.otherModuleNames(location.getProject(), module(location))
                          .filter(n -> n.startsWith(finalPrefix))
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
