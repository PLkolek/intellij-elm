package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

import java.util.Collection;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class ElmModuleHeader extends ASTWrapperPsiElement {

    public ElmModuleHeader(ASTNode node) {
        super(node);
    }

    public ElmModuleName moduleName() {
        return findChildOfType(this, ElmModuleName.class);
    }

    public Optional<ElmModuleValueList> exposingList() {
        ElmExposingNode exposingNode = findChildOfType(this, ElmExposingNode.class);
        return Optional.ofNullable(exposingNode).map(ElmExposingNode::valueList);
    }

    public Collection<ElmTypeExport> typeExports() {
        return exposingList().map(ElmModuleValueList::exportedTypes).orElse(newArrayList());
    }


    public Optional<ElmTypeExport> typeExport(String typeName) {
        return typeExports().stream().filter(export -> typeName.equals(export.typeNameString())).findFirst();
    }
}
