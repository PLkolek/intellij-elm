package mkolaczek.elm.psi.node.extensions;


import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.ExposingNode;
import mkolaczek.elm.psi.node.ModuleValueList;
import mkolaczek.elm.psi.node.TypeExposing;
import mkolaczek.elm.psi.node.ValueExposing;
import mkolaczek.util.Streams;

import java.util.Optional;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

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
}
