// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ElmModuleValueList extends ASTWrapperPsiElement {

    public ElmModuleValueList(ASTNode node) {
        super(node);
    }

    public Collection<ElmExportedValue> values() {
        return PsiTreeUtil.findChildrenOfType(this, ElmExportedValue.class);
    }

    public boolean isOpenListing() {
        return PsiTreeUtil.getChildOfType(this, ElmOpenListing.class) != null;
    }

    public Collection<ElmTypeExport> exportedTypes() {
        return values().stream()
                       .map(ElmExportedValue::typeExport)
                       .filter(Optional::isPresent)
                       .map(Optional::get)
                       .collect(toList());
    }
}
