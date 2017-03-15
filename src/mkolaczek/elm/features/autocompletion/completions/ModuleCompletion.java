package mkolaczek.elm.features.autocompletion.completions;

import com.google.common.base.Preconditions;
import com.intellij.codeInsight.completion.CompletionParameters;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.features.autocompletion.Names;
import mkolaczek.elm.features.autocompletion.Patterns;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.ModuleNameRef;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

public class ModuleCompletion {
    public static void modules(ElmCompletionContributor c) {
        c.autocomplete(Patterns.afterLeaf(Tokens.MODULE), ModuleCompletion::fileName);
        c.autocomplete(Patterns.afterLeaf(Tokens.AS), ModuleCompletion::moduleNameParts);
    }

    @NotNull
    private static Collection<String> moduleNameParts(CompletionParameters parameters) {
        Import importLine = getParentOfType(parameters.getPosition(), Import.class);
        Preconditions.checkState(importLine != null, "As must be a child of import line");
        ModuleNameRef module = importLine.importedModuleName();
        String[] words = module.getName().split("\\.");
        return Names.suggest(words);
    }

    @NotNull
    private static Collection<String> fileName(CompletionParameters parameters) {
        String fileName = parameters.getOriginalFile().getName();
        String moduleName = fileName.substring(0, fileName.length() - 4);//cut out .elm
        return newArrayList(moduleName);
    }
}
