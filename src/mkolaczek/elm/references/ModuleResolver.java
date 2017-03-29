package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.builtInImports.BuiltInImport;
import mkolaczek.elm.builtInImports.BuiltInImports;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.ModuleNameRef;
import mkolaczek.util.Streams;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
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
                        .filter(i -> target.getName().equals(i.importedModuleNameString().orElse(null)))
                        .flatMap(import_ -> ProjectUtil.modules(import_.getProject(),
                                import_.importedModuleNameString().orElse(null))),

                module(target)
                        .aliasedImports()
                        .map(Import::alias)
                        .flatMap(Streams::stream)
                        .filter(a -> a.sameName(target.getName())),

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
        return Stream.of(
                module(target)
                        .notAliasedImports()
                        .filter(i -> target.getName().equals(i.importedModuleNameString().orElse(null)))
                        .flatMap(import_ -> ProjectUtil.modules(import_.getProject(),
                                import_.importedModuleNameString().orElse(null))),

                module(target)
                        .aliasedImports()
                        .filter(i -> i.importedAs(target.getName()))
                        .flatMap(import_ -> ProjectUtil.modules(import_.getProject(),
                                import_.importedModuleNameString().orElse(null))),

                BuiltInImports.imports()
                              .filter(i -> i.importedAs().equals(target.getName()))
                              .flatMap(i -> ProjectUtil.modules(target.getProject(), i.moduleName()))
        ).flatMap(Function.identity());
    }

    public static Stream<String> variants(PsiElement target) {
        Module module = module(target);


        Optional<Stream<Module>> inImport = inImport(target);

        return inImport.map(
                moduleStream -> Stream.concat(
                        moduleStream.map(Module::getName),
                        BuiltInImports.moduleNames()
                )
        ).orElseGet(() -> {
                    Set<String> alreadyImported = module.imports()
                                                        .map(Import::importedModuleNameString)
                                                        .flatMap(Streams::stream)
                                                        .collect(toSet());
                    alreadyImported.addAll(BuiltInImports.moduleNames().collect(toSet()));
                    return Stream.of(
                            module.notAliasedImports()
                                  .map(Import::importedModuleNameString)
                                  .flatMap(Streams::stream),
                            module.aliasedImports()
                                  .map(Import::aliasName)
                                  .flatMap(Streams::stream),
                            BuiltInImports.imports()
                                          .map(BuiltInImport::importedAs),
                            ProjectUtil.modules(target.getProject()).filter(m -> !alreadyImported.contains(m.getName()))
                                       .map(Module::getName)
                    ).flatMap(Function.identity());
                }
        ).filter(name -> !module.sameName(name));
    }

    private static Optional<Stream<Module>> inImport(PsiElement target) {
        if (PsiTreeUtil.getParentOfType(target, Import.class) != null) {
            Stream<Module> result = ProjectUtil.modules(target.getProject());
            return Optional.of(result);
        }
        return Optional.empty();
    }

}
