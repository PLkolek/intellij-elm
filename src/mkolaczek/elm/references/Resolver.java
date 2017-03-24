package mkolaczek.elm.references;


import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.extensions.HasExposing;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Resolver<T> {

    private final Function<Module, T> declared;
    private final BiFunction<HasExposing, T, T> exposedFilter;
    private final Function<T, Stream<? extends PsiNamedElement>> toStream;

    private Resolver(Function<Module, T> declared,
                     BiFunction<HasExposing, T, T> exposedFilter,
                     Function<T, Stream<? extends PsiNamedElement>> toStream) {
        this.declared = declared;
        this.exposedFilter = exposedFilter;
        this.toStream = toStream;
    }

    public static Resolver<?> forTypeConstructors() {
        return new Resolver<>(
                m -> TypeDeclaration.constructorsMultimap(m.declarations(TypeOfDeclaration.TYPE).collect(toSet())),
                HasExposing::filterExposedConstructors,
                m -> m.values().stream()
        );
    }

    public static Resolver<?> forValues() {
        return new Resolver<>(
                Module::declaredValues,
                HasExposing::filterExposedValues,
                x -> x
        );
    }


    public Stream<String> resolve(Module module) {
        Stream<? extends PsiNamedElement> exposedValues = module.imports().flatMap(
                i -> i.importedModule().flatMap(m -> {
                    T decls = declared.apply(m);
                    if (m.header().isPresent()) {
                        decls = exposedFilter.apply(m.header().get(), decls);
                    }
                    return toStream.apply(exposedFilter.apply(i, decls));
                })
        );
        return Stream.concat(toStream.apply(declared.apply(module)), exposedValues).map(PsiNamedElement::getName);
    }
}
