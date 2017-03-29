package mkolaczek.elm.references;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.builtInImports.BuiltInImports;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.extensions.*;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.psi.PsiUtil.*;
import static mkolaczek.elm.psi.node.Module.module;

public class Resolver<T> {

    private final Function<Module, T> declared;
    private final BiFunction<HasExposing, T, T> exposedFilter;
    private final Function<T, Stream<? extends PsiNamedElement>> toStream;
    private final Collection<String> builtIn;
    private final boolean includeLocal;

    private Resolver(Function<Module, T> declared,
                     BiFunction<HasExposing, T, T> exposedFilter,
                     Function<T, Stream<? extends PsiNamedElement>> toStream,
                     Collection<String> builtIn,
                     boolean includeLocal
    ) {
        this.declared = declared;
        this.exposedFilter = exposedFilter;
        this.toStream = toStream;
        this.builtIn = builtIn;
        this.includeLocal = includeLocal;
    }

    public static Resolver<?> forTypeConstructors() {
        return new Resolver<>(
                m -> TypeDeclaration.constructorsMultimap(m.declarations(TypeOfDeclaration.TYPE).collect(toSet())),
                HasExposing::filterExposedConstructors,
                m -> m.values().stream(),
                ImmutableList.of(),
                false
        );
    }

    public static Resolver<?> forOperators() {
        return new Resolver<>(
                m -> m.declarations(TypeOfDeclaration.OPERATOR),
                ((hasExposing, declarations) -> hasExposing.filterExposed(declarations, TypeOfExposed.OPERATOR)),
                identity(),
                ImmutableList.of(),
                false
        );
    }


    public static Resolver<?> forValues() {
        return new Resolver<>(
                Module::declaredValues,
                ((hasExposing, declarations) -> hasExposing.filterExposed(declarations, TypeOfExposed.VALUE)),
                identity(),
                ImmutableList.of(),
                true
        );
    }

    public static Resolver<?> forTypes() {
        return new Resolver<>(
                m -> m.declarations(TypeOfDeclaration.TYPE),
                ((hasExposing, declarations) -> hasExposing.filterExposed(declarations, TypeOfExposed.TYPE)),
                Function.identity(),
                BuiltInImports.typeNames(),
                false
        );
    }

    public Stream<? extends PsiNamedElement> resolve(PsiNamedElement target) {
        if (target.getName() == null) {
            return Stream.empty();
        }
        assert target.getName() != null;
        Optional<Stream<? extends PsiNamedElement>> qualified = inSpecificLocations(target);
        if (qualified.isPresent()) {
            return qualified.get().filter(e -> target.getName().equals(e.getName()));
        }
        Module module = module(target);
        Stream<Stream<? extends PsiNamedElement>> locals = locals(target);
        Stream<? extends PsiNamedElement> declared1 = declared(module);
        Stream<PsiNamedElement> exposedValues = exposed(module);
        List<? extends PsiNamedElement> found = locals.map(s -> s.filter(e -> target.getName().equals(e.getName()))
                                                                 .collect(toList()))
                                                      .filter(c -> !c.isEmpty())
                                                      .findFirst()
                                                      .orElse(Lists.newArrayList());
        if (!found.isEmpty()) {
            return found.stream();
        }
        found = declared1.filter(e -> target.getName().equals(e.getName())).collect(toList());
        if (!found.isEmpty()) {
            return found.stream();
        }
        return exposedValues.filter(e -> target.getName().equals(e.getName()));

    }

    public Stream<String> variants(PsiElement target) {
        Optional<Stream<? extends PsiNamedElement>> specific = inSpecificLocations(target);
        if (specific.isPresent()) {
            return specific.get().map(PsiNamedElement::getName);
        }

        return unqualifiedVariants(target);
    }

    public Stream<String> unqualifiedVariants(PsiElement target) {
        Module module = module(target);
        Stream<? extends PsiNamedElement> locals = locals(target).flatMap(identity());
        Stream<? extends PsiNamedElement> exposedValues = exposed(module);
        Stream<? extends PsiNamedElement> declared = declared(module);
        return Stream.concat(
                Stream.of(locals, declared, exposedValues)
                      .flatMap(identity())
                      .map(PsiNamedElement::getName),
                builtIn.stream()
        );
    }

    private Optional<Stream<? extends PsiNamedElement>> inSpecificLocations(PsiElement target) {
        Module module = module(target);
        if (insideModuleHeader(target)) {
            return Optional.of(declared(module(target)));
        } else if (insideImport(target)) {
            Import import_ = containingImport(target);
            return Optional.of(importedModules(import_.getProject(), import_.importedModuleNameString().orElse(null))
                    .flatMap(m -> toStream.apply(declaredAndExposedFrom(m)))
            );
        }

        QualifiedRef qualifiedRef = getParentOfType(target, QualifiedRef.class);
        if (qualifiedRef != null && qualifiedRef.moduleName().isPresent()) {
            return Optional.of(
                    Stream.concat(module.imports()
                                        .map(i -> i.importedModuleNameString().orElse(null)),
                            BuiltInImports.moduleNames())
                          .filter(s -> qualifiedRef.moduleName().get().getName().equals(s))
                          .flatMap(moduleName -> importedModules(target.getProject(), moduleName))
                          .flatMap(m -> toStream.apply(declaredAndExposedFrom(m)))
            );
        }
        return Optional.empty();
    }

    private Stream<Module> importedModules(Project project, String moduleName) {
        return ProjectUtil.modules(project, moduleName);
    }

    public Stream<Stream<? extends PsiNamedElement>> locals(PsiElement target) {
        Stream<Stream<? extends PsiNamedElement>> locals = Stream.empty();
        if (includeLocal) {
            Stream<DefinesValues> lets = StreamEx.iterate(getParentOfType(target, DefinesValues.class),
                    Objects::nonNull,
                    e -> getParentOfType(e, DefinesValues.class));
            locals = lets.map(DefinesValues::declaredValues);
        }
        return locals;
    }

    public Stream<PsiNamedElement> exposed(Module module) {
        return module.imports().flatMap(
                i -> importedModules(i.getProject(), i.importedModuleNameString().orElse(null)).flatMap(
                        m -> toStream.apply(exposedFilter.apply(i, declaredAndExposedFrom(m)))
                )
        );
    }

    public Stream<? extends PsiNamedElement> declared(Module module) {
        return toStream.apply(declared.apply(module));
    }

    private T declaredAndExposedFrom(Module m) {
        T decls = declared.apply(m);
        if (m.header().isPresent()) {
            decls = exposedFilter.apply(m.header().get(), decls);
        }
        return decls;
    }
}
