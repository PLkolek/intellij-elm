package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.builtInImports.BuiltInImports;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.ModuleNameRef;
import mkolaczek.util.Streams;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static mkolaczek.elm.psi.node.Module.module;

public class ModuleResolver {

    public static Stream<PsiElement> resolveToAlias(ModuleNameRef target) {
        Optional<Stream<Module>> inImport = inImport(target);
        if (inImport.isPresent()) {
            return inImport.get().filter(m -> m.sameName(target.getName())).map(x -> x);
        }
        return Stream.of(
                module(target)
                        .notAliasedImports()
                        .flatMap(import_ -> ProjectUtil.modules(import_.getProject(),
                                import_.importedModuleNameString().orElse(null))),
                module(target)
                        .aliasedImports()
                        .map(Import::alias)
                        .flatMap(Streams::stream),
                BuiltInImports.imports()
                              .filter(i -> i.importedAs().equals(target.getName()))
                              .flatMap(i -> ProjectUtil.modules(target.getProject(), i.moduleName()))
        ).flatMap(Function.identity());
    }

    public static Stream<Module> resolveToModule(ModuleNameRef target) {
        Optional<Stream<Module>> inImport = inImport(target);
        if (inImport.isPresent()) {
            return inImport.get().filter(m -> m.sameName(target.getName()));
        }
        return Stream.empty();
    }

    public static Stream<String> variants(PsiElement target) {
        Module module = module(target);
        Optional<Stream<Module>> inImport = inImport(target);
        if (inImport.isPresent()) {
            return Stream.concat(
                    inImport.get().map(Module::getName),
                    BuiltInImports.moduleNames()
            ).filter(name -> !module.sameName(name));
        }
        return Stream.empty();
    }

    private static Optional<Stream<Module>> inImport(PsiElement target) {
        if (PsiTreeUtil.getParentOfType(target, Import.class) != null) {
            Stream<Module> result = ProjectUtil.modules(target.getProject());
            return Optional.of(result);
        }
        return Optional.empty();
    }

}
