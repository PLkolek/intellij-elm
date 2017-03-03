package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

import java.util.Optional;

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
}
