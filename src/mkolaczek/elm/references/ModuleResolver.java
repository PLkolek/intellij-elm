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

    public static Stream<? extends PsiElement> resolveToAlias(ModuleNameRef target) {
        return resolve(target, aliased -> aliased.map(Import::alias)
                                                 .flatMap(Streams::stream)
                                                 .filter(a -> a.sameName(target.getName()))
                                                 .map(x -> x));
    }

    public static Stream<Module> resolveToModule(ModuleNameRef target) {
        return resolve(target,
                aliased -> aliased.filter(i -> i.importedAs(target.getName()))
                                  .flatMap(import_ -> {
                                      String moduleName = import_.importedModuleNameString().orElse(null);
                                      return ProjectUtil.modules(import_.getProject(), moduleName);
                                  })
        ).map(m -> (Module) m);
    }

    private static Stream<? extends PsiElement> resolve(ModuleNameRef target,
                                                        Function<Stream<Import>, Stream<PsiElement>> aliasedTransform) {
        Optional<Stream<Module>> inImport = inImport(target);
        if (inImport.isPresent()) {
            return inImport.get().filter(m -> m.sameName(target.getName()));
        }
        return Stream.of(
                sameNameNotAliased(target),
                aliasedTransform.apply(aliasedImports(target)),
                sameNameBuiltIn(target)
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
                            notAliasedImports(target).map(Import::importedModuleNameString)
                                                     .flatMap(Streams::stream),

                            aliasedImports(target).map(Import::aliasName)
                                                  .flatMap(Streams::stream),

                            BuiltInImports.imports().map(BuiltInImport::importedAs),

                            ProjectUtil.modules(target.getProject())
                                       .filter(m -> !alreadyImported.contains(m.getName()))
                                       .map(Module::getName)
                    ).flatMap(Function.identity());
                }
        ).filter(name -> !module.sameName(name));
    }

    private static Stream<Import> notAliasedImports(PsiElement target) {
        return module(target).notAliasedImports();
    }

    private static Stream<Module> sameNameBuiltIn(ModuleNameRef target) {
        return BuiltInImports.imports()
                             .filter(i -> i.importedAs().equals(target.getName()))
                             .flatMap(i -> ProjectUtil.modules(target.getProject(), i.moduleName()));
    }

    private static Stream<Module> sameNameNotAliased(ModuleNameRef target) {
        return notAliasedImports(target)
                .filter(i -> target.getName().equals(i.importedModuleNameString().orElse(null)))
                .flatMap(import_ -> ProjectUtil.modules(import_.getProject(),
                        import_.importedModuleNameString().orElse(null)));
    }

    public static Stream<Import> aliasedImports(PsiElement target) {
        return module(target).aliasedImports();
    }

    private static Optional<Stream<Module>> inImport(PsiElement target) {
        if (PsiTreeUtil.getParentOfType(target, Import.class) != null) {
            Stream<Module> result = ProjectUtil.modules(target.getProject());
            return Optional.of(result);
        }
        return Optional.empty();
    }

}
