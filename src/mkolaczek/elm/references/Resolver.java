package mkolaczek.elm.references;


import com.google.common.collect.Maps;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.extensions.DefinesValues;
import mkolaczek.elm.psi.node.extensions.HasExposing;
import mkolaczek.elm.psi.node.extensions.QualifiedRef;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import one.util.streamex.StreamEx;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.psi.node.Module.module;

public class Resolver<T> {

    private final Function<Module, T> declared;
    private final BiFunction<HasExposing, T, T> exposedFilter;
    private final Function<T, Stream<? extends PsiNamedElement>> toStream;
    private final boolean includeLocal;

    private Resolver(Function<Module, T> declared,
                     BiFunction<HasExposing, T, T> exposedFilter,
                     Function<T, Stream<? extends PsiNamedElement>> toStream,
                     boolean includeLocal
    ) {
        this.declared = declared;
        this.exposedFilter = exposedFilter;
        this.toStream = toStream;
        this.includeLocal = includeLocal;
    }

    public static Resolver<?> forTypeConstructors() {
        return new Resolver<>(
                m -> TypeDeclaration.constructorsMultimap(m.declarations(TypeOfDeclaration.TYPE).collect(toSet())),
                HasExposing::filterExposedConstructors,
                m -> m.values().stream(),
                false
        );
    }

    public static Resolver<?> forValues() {
        return new Resolver<>(
                Module::declaredValues,
                HasExposing::filterExposedValues,
                x -> x,
                true
        );
    }


    public Stream<PsiNamedElement> resolve(PsiNamedElement target) {
        if (target.getName() == null) {
            return Stream.empty();
        }
        Optional<Stream<PsiNamedElement>> qualified = qualified(target);
        if (qualified.isPresent()) {
            return qualified.get();
        }
        Module module = module(target);
        Stream<PsiNamedElement> locals = locals(target);
        Stream<? extends PsiNamedElement> declared = declared(module);
        Stream<PsiNamedElement> exposedValues = exposed(module);
        Optional<? extends PsiNamedElement> found;
        found = locals.filter(e -> target.getName().equals(e.getName()))
                      .findFirst();
        if (found.isPresent()) {
            return Stream.of(found.get());
        }
        found = declared.filter(e -> target.getName().equals(e.getName()))
                        .findFirst();
        if (found.isPresent()) {
            return Stream.of(found.get());
        }
        return exposedValues.filter(e -> target.getName().equals(e.getName()));
    }

    public Stream<String> variants(PsiElement target) {
        Optional<Stream<PsiNamedElement>> qualified = qualified(target);
        if (qualified.isPresent()) {
            return qualified.get().map(PsiNamedElement::getName);
        }

        return unqualifiedVariants(target);
    }

    public Stream<String> unqualifiedVariants(PsiElement target) {
        Module module = module(target);
        Stream<? extends PsiNamedElement> locals = locals(target);
        Stream<? extends PsiNamedElement> exposedValues = exposed(module);
        Stream<? extends PsiNamedElement> declared = declared(module);
        return Stream.of(locals, declared, exposedValues)
                     .flatMap(Function.identity())
                     .map(PsiNamedElement::getName);
    }

    private Optional<Stream<PsiNamedElement>> qualified(PsiElement target) {
        Module module = module(target);
        QualifiedRef qualifiedRef = getParentOfType(target, QualifiedRef.class);
        if (qualifiedRef != null && qualifiedRef.moduleName().isPresent()) {
            return Optional.of(
                    module.imports(qualifiedRef.moduleName().get().getName())
                          .flatMap(Import::importedModule)
                          .flatMap(m -> removeDuplicates(toStream.apply(declaredAndExposedFrom(m))))
            );
        }
        return Optional.empty();
    }

    public Stream<PsiNamedElement> locals(PsiElement target) {
        Stream<PsiNamedElement> locals = Stream.empty();
        if (includeLocal) {
            Stream<DefinesValues> lets = StreamEx.iterate(getParentOfType(target, DefinesValues.class),
                    Objects::nonNull,
                    e -> getParentOfType(e, DefinesValues.class));
            locals = lets.flatMap(DefinesValues::declaredValues);
        }
        return locals;
    }

    public Stream<PsiNamedElement> exposed(Module module) {
        return module.imports().flatMap(
                i -> i.importedModule().flatMap(
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

    private Stream<? extends PsiNamedElement> removeDuplicates(Stream<? extends PsiNamedElement> items) {
        Map<String, PsiNamedElement> unique = Maps.newLinkedHashMap();
        items.forEachOrdered(e -> {
            if (!unique.containsKey(e.getName())) {
                unique.put(e.getName(), e);
            }
        });
        return unique.values().stream();
    }
}