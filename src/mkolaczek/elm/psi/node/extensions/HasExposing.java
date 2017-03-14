package mkolaczek.elm.psi.node.extensions;


import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.ExposingNode;
import mkolaczek.elm.psi.node.ModuleValueList;
import mkolaczek.elm.psi.node.TypeExport;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public abstract class HasExposing extends ASTWrapperPsiElement {

    public HasExposing(@NotNull ASTNode node) {
        super(node);
    }

    public Optional<ModuleValueList> exposingList() {
        ExposingNode exposingNode = findChildOfType(this, ExposingNode.class);
        return Optional.ofNullable(exposingNode).map(ExposingNode::valueList);
    }

    public Collection<TypeExport> typeExports() {
        return exposingList().map(ModuleValueList::exportedTypes).orElse(newArrayList());
    }


    public Optional<TypeExport> typeExport(String typeName) {
        return typeExports().stream().filter(export -> typeName.equals(export.typeNameString())).findFirst();
    }
}
