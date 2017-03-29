package mkolaczek.elm.psi.node.extensions;

import com.google.common.collect.Multimap;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.TypeConstructor;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static mkolaczek.elm.psi.node.extensions.ElmNamedElement.nameIn;
import static mkolaczek.util.Collectors.toMultimap;

public interface HasExposing {

    default Stream<? extends PsiNamedElement> filterExposed(Stream<? extends PsiNamedElement> elements,
                                                            TypeOfExposed typeOfExposed) {
        if (exposesEverything()) {
            return elements;
        }
        return elements.filter(nameIn(exposed(typeOfExposed).map(Exposed::exposedName)));
    }

    default Multimap<String, TypeConstructor> filterExposedConstructors(Multimap<String, TypeConstructor> constructors) {
        if (exposesEverything()) {
            return constructors;
        }

        Map<String, Exposed> exposedTypes =
                exposed(TypeOfExposed.TYPE).collect(toMap(
                        Exposed::exposedName,
                        identity()
                ));

        return constructors.entries().stream()
                           .filter(e -> exposedTypes.containsKey(e.getKey()))
                           .filter(e -> exposedTypes.get(e.getKey()).exposes(e.getValue().getName()))
                           .collect(toMultimap(Map.Entry::getKey, Map.Entry::getValue));
    }

    boolean exposesEverything();

    Stream<Exposed> exposed(TypeOfExposed exposedElementsType);
}
