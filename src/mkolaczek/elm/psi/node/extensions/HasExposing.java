package mkolaczek.elm.psi.node.extensions;


import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.ExposingNode;
import mkolaczek.elm.psi.node.ModuleValueList;
import mkolaczek.elm.psi.node.TypeExport;
import mkolaczek.util.Streams;

import java.util.Optional;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public interface HasExposing extends PsiElement {

    default Optional<ModuleValueList> exposingList() {
        ExposingNode exposingNode = findChildOfType(this, ExposingNode.class);
        return Optional.ofNullable(exposingNode).map(ExposingNode::valueList);
    }

    default Optional<TypeExport> typeExport(String typeName) {
        return Streams.stream(exposingList())
                      .flatMap(l -> l.exported(TypeOfExport.TYPE))
                      .filter(export -> typeName.equals(export.typeNameString())).findFirst();
    }

    default <T extends PsiElement> Stream<T> exports(TypeOfExport<T> exposedElementsType) {
        return Streams.stream(exposingList()).flatMap(l -> l.exported(exposedElementsType));
    }
}
