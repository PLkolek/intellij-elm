package mkolaczek.elm.psi.node.extensions;


import com.google.common.collect.Multimap;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.psi.node.*;
import mkolaczek.util.Streams;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static mkolaczek.elm.psi.node.extensions.ElmNamedElement.nameIn;
import static mkolaczek.util.Collectors.toMultimap;

public interface HasExposing extends PsiElement {

    default Optional<ModuleValueList> exposingList() {
        ExposingNode exposingNode = findChildOfType(this, ExposingNode.class);
        return Optional.ofNullable(exposingNode).map(ExposingNode::valueList);
    }

    default Optional<TypeExposing> exposedType(String typeName) {
        return exposed(TypeOfExposed.TYPE)
                .filter(export -> typeName.equals(export.typeNameString())).findFirst();
    }

    default Stream<ValueExposing> exposedValue(String valueName) {
        return exposed(TypeOfExposed.VALUE)
                .filter(export -> valueName.equals(export.getName()));
    }

    default <T extends PsiElement> Stream<T> exposed(TypeOfExposed<T> exposedElementsType) {
        return Streams.stream(exposingList()).flatMap(l -> l.exposed(exposedElementsType));
    }

    default Multimap<String, TypeConstructor> filterExposedConstructors(Multimap<String, TypeConstructor> constructors) {
        if (exposesEverything()) {
            return constructors;
        }

        Map<String, TypeExposing> exposedTypes =
                exposed(TypeOfExposed.TYPE).collect(toMap(
                        TypeExposing::typeNameString,
                        identity()
                ));

        return constructors.entries().stream()
                           .filter(e -> exposedTypes.containsKey(e.getKey()))
                           .filter(e -> exposedTypes.get(e.getKey()).exposes(e.getValue()))
                           .collect(toMultimap(Map.Entry::getKey, Map.Entry::getValue));
    }

    default Stream<PsiNamedElement> filterExposedValues(Stream<PsiNamedElement> t) {
        if (exposesEverything()) {
            return t;
        }
        return t.filter(nameIn(exposed(TypeOfExposed.VALUE)));
    }

    default Boolean exposesEverything() {
        return exposingList().map(ModuleValueList::isOpenListing).orElse(noExposingExposesAll());
    }

    boolean noExposingExposesAll();

}
