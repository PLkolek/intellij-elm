package mkolaczek.elm.references;


import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.extensions.HasExposing;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class Resolver {
    public static <T> Stream<String> aaa(Module module,
                                         Function<Module, T> declared,
                                         BiFunction<HasExposing, T, T> exposedFilter,
                                         Function<T, Stream<? extends PsiNamedElement>> toStream) {
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
