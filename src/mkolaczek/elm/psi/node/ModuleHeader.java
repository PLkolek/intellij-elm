package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

import java.util.Collection;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class ModuleHeader extends ASTWrapperPsiElement {

    public ModuleHeader(ASTNode node) {
        super(node);
    }

    public ModuleName moduleName() {
        return findChildOfType(this, ModuleName.class);
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
