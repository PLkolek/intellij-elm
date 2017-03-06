// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.util.Optionals;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class ElmModuleValueList extends ASTWrapperPsiElement {

    public ElmModuleValueList(ASTNode node) {
        super(node);
    }

    public <T extends PsiElement> Collection<T> values(Class<T> nodeType) {
        return PsiTreeUtil.findChildrenOfType(this, nodeType);
    }

    public boolean isOpenListing() {
        return PsiTreeUtil.getChildOfType(this, ElmOpenListing.class) != null;
    }

    public Collection<ElmTypeExport> exportedTypes() {
        return values(ElmExportedValue.class).stream()
                                             .map(ElmExportedValue::typeExport)
                                             .flatMap(Optionals::stream)
                                             .collect(toList());
    }
}
