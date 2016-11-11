// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;

public class ElmModuleValueList extends ASTWrapperPsiElement {

    public ElmModuleValueList(ASTNode node) {
        super(node);
    }

    public Collection<ElmExportedValue> values() {
        return PsiTreeUtil.findChildrenOfType(this, ElmExportedValue.class);
    }

    public boolean isOpenListing() {
        return PsiTreeUtil.findChildOfType(this, ElmOpenListing.class) != null;
    }
}
