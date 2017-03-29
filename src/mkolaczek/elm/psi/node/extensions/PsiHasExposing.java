package mkolaczek.elm.psi.node.extensions;


import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.ExposingNode;
import mkolaczek.elm.psi.node.ModuleValueList;
import mkolaczek.util.Streams;

import java.util.Optional;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public interface PsiHasExposing extends PsiElement, HasExposing {

    default Optional<ModuleValueList> exposingList() {
        ExposingNode exposingNode = findChildOfType(this, ExposingNode.class);
        return Optional.ofNullable(exposingNode).map(ExposingNode::valueList);
    }

    default Optional<Exposed> exposedType(String typeName) {
        return exposed(TypeOfExposed.TYPE)
                .filter(export -> typeName.equals(export.exposedName())).findFirst();
    }

    @Override
    default Stream<Exposed> exposed(TypeOfExposed exposedElementsType) {
        return Streams.stream(exposingList()).flatMap(l -> l.exposed(exposedElementsType));
    }

    @Override
    default boolean exposesEverything() {
        return exposingList().map(ModuleValueList::isOpenListing).orElse(noExposingExposesAll());
    }

    boolean noExposingExposesAll();

}
