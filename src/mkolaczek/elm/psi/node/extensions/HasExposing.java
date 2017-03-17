package mkolaczek.elm.psi.node.extensions;


import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.*;
import mkolaczek.util.Streams;

import java.util.Optional;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public interface HasExposing extends PsiElement {

    default Optional<ModuleValueList> exposingList() {
        ExposingNode exposingNode = findChildOfType(this, ExposingNode.class);
        return Optional.ofNullable(exposingNode).map(ExposingNode::valueList);
    }

    default Stream<TypeExport> typeExports() {
        return Streams.stream(exposingList()).flatMap(ModuleValueList::exportedTypes);
    }

    default Stream<Operator> operatorExports() {
        return Streams.stream(exposingList()).flatMap(ModuleValueList::exportedOperators);
    }

    default Stream<ValueExport> valueExports() {
        return Streams.stream(exposingList()).flatMap(ModuleValueList::exportedValues);
    }

    default Optional<TypeExport> typeExport(String typeName) {
        return typeExports().filter(export -> typeName.equals(export.typeNameString())).findFirst();
    }
}
