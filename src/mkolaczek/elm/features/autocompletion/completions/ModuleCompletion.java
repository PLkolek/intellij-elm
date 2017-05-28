package mkolaczek.elm.features.autocompletion.completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.features.autocompletion.Names;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.ModuleNameRef;
import mkolaczek.elm.psi.node.extensions.QualifiedRef;
import mkolaczek.elm.references.ModuleResolver;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static mkolaczek.elm.features.autocompletion.ElmCompletionContributor.location;
import static mkolaczek.elm.features.autocompletion.Patterns.*;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.DOT;
import static mkolaczek.elm.psi.Tokens.RUNE_OF_AUTOCOMPLETION;

public class ModuleCompletion {
    public static void modules(ElmCompletionContributor c) {
        //@formatter:off
        c.autocomplete(afterLeaf(Tokens.MODULE),                            ModuleCompletion::fileName);
        c.autocomplete(afterLeaf(Tokens.AS),                                ModuleCompletion::moduleNameParts);
        c.autocomplete(inside(MODULE_NAME_REF).inside(e(IMPORT_LINE)),      ModuleCompletion::modules);
        c.autocomplete(inside(MODULE_NAME_REF).andNot(inside(IMPORT_LINE)), ModuleCompletion::dotModules);
        c.autocomplete(e(RUNE_OF_AUTOCOMPLETION).inside(e(PATTERN_TERM)).andNot(afterLeaf(DOT)),   ModuleCompletion::dotModules);
        c.autocomplete(inside(QUALIFIED_TYPE_NAME_REF),                     ModuleCompletion::matchingModules);
        c.autocomplete(inside(QUALIFIED_TYPE_CONSTRUCTOR_REF),              ModuleCompletion::matchingModules);
        c.autocomplete(inside(QUALIFIED_VAR),                               ModuleCompletion::matchingModules);
        //@formatter:on
    }

    private static Stream<String> dotModules(CompletionParameters parameters) {
        return ModuleResolver.variants(parameters.getPosition()).map(s -> s + ".");
    }

    private static Stream<String> modules(CompletionParameters parameters) {
        return ModuleResolver.variants(parameters.getPosition());
    }

    private static Stream<String> matchingModules(CompletionParameters parameters) {
        QualifiedRef qualifiedType = location(parameters, QualifiedRef.class);
        String prefix = qualifiedType.moduleName().map(ModuleNameRef::getName).orElse("");
        prefix = prefix.length() > 0 ? prefix + "." : "";

        return matchingModules(qualifiedType, prefix);
    }

    private static Stream<String> matchingModules(PsiElement location, String finalPrefix) {
        return ModuleResolver.variants(location)
                             .filter(n -> n.startsWith(finalPrefix))
                             .map(n -> Names.suffix(n, finalPrefix))
                             .filter(n -> !n.isEmpty())
                             .map(n -> n + ".");
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
