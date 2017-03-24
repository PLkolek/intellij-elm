package mkolaczek.elm.references;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.LetExpression;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.extensions.HasExposing;
import mkolaczek.elm.psi.node.extensions.QualifiedRef;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import one.util.streamex.StreamEx;

import java.util.Objects;
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


    public Stream<String> resolve(PsiElement target) {
        Module module = module(target);
        QualifiedRef qualifiedRef = getParentOfType(target, QualifiedRef.class);
        if (qualifiedRef != null && qualifiedRef.moduleName().isPresent()) {
            return module.imports(qualifiedRef.moduleName().get().getName())
                         .flatMap(Import::importedModule)
                         .flatMap(toStream.compose(this::declaredAndExposedFrom))
                         .map(PsiNamedElement::getName);
        }

        return resolveUnqualified(target);
    }

    public Stream<String> resolveUnqualified(PsiElement target) {
        Module module = module(target);
        Stream<? extends PsiNamedElement> locals = Stream.empty();
        if (includeLocal) {
            Stream<LetExpression> lets = StreamEx.iterate(getParentOfType(target, LetExpression.class),
                    Objects::nonNull,
                    e -> getParentOfType(e, LetExpression.class));
            locals = lets.flatMap(LetExpression::declaredValues);
        }

        Stream<? extends PsiNamedElement> exposedValues = module.imports().flatMap(
                i -> i.importedModule().flatMap(
                        m -> toStream.apply(exposedFilter.apply(i, declaredAndExposedFrom(m)))
                )
        );
        return Stream.of(locals, toStream.apply(declared.apply(module)), exposedValues)
                     .flatMap(Function.identity())
                     .map(PsiNamedElement::getName);
    }

    private T declaredAndExposedFrom(Module m) {
        T decls = declared.apply(m);
        if (m.header().isPresent()) {
            decls = exposedFilter.apply(m.header().get(), decls);
        }
        return decls;
    }
}
